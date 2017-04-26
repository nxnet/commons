#!/bin/bash
#
# Checkstyle checks utility functions
#

#=======================
# Shell execution setup
#=======================
set -o errexit
set -o pipefail
set -o nounset
set -o xtrace

#=================
# Magic variables
#=================
declare __dir="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
declare __file="${__dir}/$(basename "${BASH_SOURCE[0]}")"
declare __base="$(basename ${__file})"
declare __root="$(cd "$(dirname "${__dir}")" && pwd)" # <-- change this as it depends on your app
declare __user=$(whoami)
declare __home="${APP_HOME:-${__dir}/..}"
declare __src="${__home}/src"
declare __lib="${__home}/lib"
declare __log="${__home}/log"
declare __cfg="${__home}/config"
declare __tmp="${__home}/tmp"
declare __classpath="${CLASSPATH:-.}" &&  for jarfile in ${__lib}/*.jar ; do __classpath=${__classpath}:${jarfile} ; done

#================================
# Script custom global variables
#================================
declare __svn_repository="${1:?Svn repository argument undefined}"
declare __svn_revision="${2:?Svn revision argument undefined}"

declare __checkstyle_jar="${__lib}/checkstyle-7.6-all.jar" # path to checkstyle jar file
declare __checkstyle_cfg="${__cfg}/etkc-conventions-2.2.0-RC1-checkstyle-ansi.xml" #path to checkstyle definitions file
declare __checkstyle_workspace="${__tmp}/${__svn_repository}/${__svn_revision}" # tmp folder used by checkstyle hook
declare __checkstyle_sources="${__checkstyle_workspace}/src" # tmp folder for saving commiting source files
declare __checkstyle_report="${__checkstyle_workspace}/checkstyle.report" # checkstyle report file

declare __svncmd="${SVNCMD:-svnlook}" # define svn utility command
declare __gitcmd="${GITCMD:-git}" # define git utility command
declare __scmtype="${SCMTYPE:?SCM type is undefined}" # define SCM dependent mode, valid values are GIT or SVN 

#================
# Script imports
#================
#source "${__dir}/actions"

#======================
# Function definitions
#======================

#
# Create workspace
#
__createWorkspace()
{
    declare checkstyle_workspace="${1:-__checkstyle_workspace}"
    mkdir -p "${checkstyle_workspace}"
}

#
# Get filepaths changed in commit
#
__getChangedPaths()
{
    declare old_revision="${1:?First argument undefined}"
    declare new_revision="${2:?Second argument undefined}"

    if [[ "${__scmtype}" == "SVN" ]] ; then
        __getChangedPathsSvn()
    else if [[ "${__scmtype}" == "GIT" ]] ; then
        __getChangedPathsGit()
}

#
# Get filepaths changed in revision for given svn repo
#
__getChangedPathsSvn()
{
    declare svn_repository="${1:?Svn repository argument undefined}"
    declare svn_revision="${2:?Svn revision argument undefined}"

    echo "$(__svncmd changed -t ${svn_revision} ${svn_repository} | grep -v '^D' | awk '{print $2}')"
}

#
# Get filepaths changed in revision for given git repo
#
__getChangedPathsGit()
{
    declare old_revision="${1:?Old revision argument undefined}"
    declare new_revision="${2:?New revision argument undefined}"

    echo "$(__gitcmd diff --name-only ${old_revision} ${new_revision})"
}

#
# Copy files in commit transaction in temporary location
#
declare __changed_paths="$(svnlook changed -t ${__svn_revision} ${__svn_repository} | grep -v '^D'| awk '{print $2}')"
for __changed_path in ${__changed_paths} ; do

     echo "> Checkstyle action detects change on path: ${__changed_path}" #>&2
     #
     # If it is a file and not directory and start with m2mse/ meaning that is m2mse project file
     #
     if ( echo "${__changed_path}" | grep -v "/$" | grep "^m2mse/" | grep -E -v "(\.jpg|\.jpeg)$" ); then

         declare __tmp_source_file="${__checkstyle_src}/${__changed_path}"
         declare __tmp_source_dir="$(dirname ${__tmp_source_file})"

         #
         # Create full dir path for temporary source file
         #
         echo "> Checkstyle action creates dir: ${__tmp_source_dir}" #>&2
         mkdir -p "${__tmp_source_dir}"

         #
         # Write temporary source file
         #
         echo "> Checkstyle action creates file: ${__tmp_source_file}" #>&2
         svnlook cat "${__svn_repository}" -t "${__svn_revision}" "${__changed_path}" > "${__tmp_source_file}"
     else
         #
         # Ignore directories
         #
         echo "> Checkstyle action ignores path: ${__changed_path}" #>&2
     fi
done

#
# Do checks and create checkstyle report
#
java -jar "${__checkstyle_jar}" -c "${__checkstyle_cfg}" -o "${__checkstyle_rpt}" "${__checkstyle_src}/" || true
declare __checkstyle_regex="^\[ERROR\]"
if( cat "${__checkstyle_rpt}" | grep "${__checkstyle_regex}" ); then

   echo "/==================================\\" >&2
   echo "| > Checkstyle Error(s) " >&2
   echo "+==================================+" >&2
   cat "${__checkstyle_rpt}" | grep "${__checkstyle_regex}" | sed -e "s@${__checkstyle_src}/@@" >&2
   echo "+==================================+" >&2
   echo "| > Commiting source code doesn't pass checkstyle checks." >&2
   echo "| > Please refactor your source code to conform to established syntax rules and try commit again." >&2
   echo "\\==================================/" >&2

   #
   # Checkstyle checks didn't pass
   #
   exit 1
else
   #
   # All checks passed, so remove checkstyle workspace
   #
   echo "> Checkstyle action checks are OK, removing workspace dir ${__checkstyle_tmp}" #>&2
   rm -Rf "${__checkstyle_tmp}"
fi
