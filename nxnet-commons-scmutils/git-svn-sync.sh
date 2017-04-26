#!/bin/bash
#
# SVN <-> GIT sync functions
#
############################

#=======================
# Shell execution setup
#=======================
set -o errexit
set -o pipefail
#set -o nounset
#set -o xtrace

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
declare __classpath="${CLASSPATH:-.}" &&  for jarfile in ${__lib}/*.jar ; do __classpath=${__classpath}:${jarfile} ; done

#================================
# Script custom global variables
#================================
declare __PREFIX="[SVN <-> GIT] "
declare __LOCKFILE="${__LOCK_FILE:?lock file path missing}"
declare __git_remote="git-mirror"
declare __svn_remote="svn-repo"

#================
# Script imports
#================
source "${__dir}/actions"

#======================
# Function definitions
#======================

# Pull from SVN and push to GIT
synchronouslyPullBranchesFromSvnAndPushToGit()
{
   # Raise lock and print execution header
   start "Sync started at $(date) in repo $(pwd) by $(whoami)..."
   # Pull from SVN and push to GIT
   pullBranchesFromSvnAndPushToGit "${@}" &&  end "Sync finished sucessfully." || end "Sync aborted!"
}

# Pull from GIT and push to SVN
synchronouslyPullBranchesFromGitAndPushToSvn()
{
   # Raise lock and print execution header
   start "Sync started at $(date) in repo $(pwd) by $(whoami)..."
   # Pull from SVN and push to GIT
   pullBranchesFromGitAndPushToSvn "${@}" && end "Sync finished sucessfully." || end "Sync aborted!"
}

# SVN -> GIT branches sync
pullBranchesFromSvnAndPushToGit()
{
   source "${__dir}/cmdarg.sh"
   cmdarg_info "header" $'Pull from SVN and push to GIT.\n\nUsage: command [flags]'
   cmdarg_info "author" "Nikola Ruzic <nikola.ruzic@ericsson.com>"
   cmdarg_info "copyright" "Copyright 2017 Nikola Ruzic"
   cmdarg 's?' 'svn-remote' 'SVN repository GIT remote definition name' "${__svn_remote}"
   cmdarg 'r?' 'svn-repository' 'SVN repository name'
   cmdarg 'p?' 'svn-project' 'SVN project name'
   cmdarg 'b?' 'branches' 'SVN branch names'
   cmdarg 'g?' 'git-remote' 'GIT mirror GIT remote definition name' "${__git_remote}"
   cmdarg 'f'  'fetch-each-branch' 'In addition to fetch all SVN remote definitions at start, fetch lazily once againg per branch basis.' 'false'
   cmdarg_parse "$@" || return 1
   validateBranchPrefixFlags

   declare svn_remote="${cmdarg_cfg['svn-remote']}"
   declare svn_repository="${cmdarg_cfg['svn-repository']}"
   declare svn_project="${cmdarg_cfg['svn-project']}"
   declare branches="${cmdarg_cfg['branches']}"
   declare git_remote="${cmdarg_cfg['git-remote']}"
   declare fetch_each_branch="$(if [[ ${cmdarg_cfg['fetch-each-branch']} == 'true' ]] ; then echo 1 ; else echo 0 ; fi)"

   # Define branch prefix
   declare branch_prefix=""
   if [[ ! -z "${svn_repository}" ]] ; then
      branch_prefix="$(branchPrefix ${svn_repository} ${svn_project})"
   fi

   # Fetch all SVN remote definitions at start
   action "Fetch from ${svn_remote}"
   git svn fetch --fetch-all && ok || failure "Unexpected exception!"

   # If branches are not explicitely specified, list remote branches filtered by prefix
   if [[ -z "${branches}" ]] ; then
      branches="$(lsBranches ${svn_remote} ${branch_prefix})"
   fi

   # Iterate through specified branches
   action "Pull branches from ${svn_remote} and push to ${git_remote}"
   for branch in ${branches} ; do
      # Construct local and remote branch names
      declare local_branch="${branch}"
      declare svn_branch="${svn_remote}/${branch}"
      declare git_branch="${git_remote}/${branch}"
      # If SVN branch name doesn't end with @[svn_revision_number_here], e.g. ...@21039
      action "Pull ${svn_branch} from ${svn_remote} and push to ${git_branch} on ${git_remote}" "\e[4m\e[34m"
      if [[ ! "${svn_branch}" =~ .*@[0-9]+$ ]] ; then
         # Pull and push branch
         pullBranchFromSvnAndPushToGit "${svn_remote}" "${svn_branch}" "${git_remote}" "${git_branch}" "${local_branch}" "${fetch_each_branch}"
         ok "" "\e[4m\e[32m"
      # Otherwise delete "strange" SVN remote branch
      else
         warning "Branch not eligible for sync!" "\e[4m\e[33m"
         action "Deleting SVN remote branch ${svn_branch}"
         git branch -d -r "${svn_branch}" && ok || failure "Unexpected exception!"
      fi
   done
   ok "Sync finished."
}

# GIT -> SVN branches sync
pullBranchesFromGitAndPushToSvn()
{
   source "${__dir}/cmdarg.sh"
   cmdarg_info "header" $'Synchronized pull from GIT and push to SVN.\n\nUsage: command [flags]'
   cmdarg_info "author" "Nikola Ruzic <nikola.ruzic@ericsson.com>"
   cmdarg_info "copyright" "Copyright 2017 Nikola Ruzic"
   cmdarg 'g?' 'git-remote' 'GIT mirror GIT remote definition name' "${__git_remote}"
   cmdarg 'b?' 'branches' 'SVN branch names'
   cmdarg 's?' 'svn-remote' 'SVN repository GIT remote definition name' "${__svn_remote}"
   cmdarg 'r?' 'svn-repository' 'SVN repository name'
   cmdarg 'p?' 'svn-project' 'SVN project name'
   cmdarg 'f'  'fetch-each-branch' 'In addition to fetch all SVN remote definitions at start, fetch lazily once againg per branch basis.' 'false'
   cmdarg_parse "$@" || return 1
   validateBranchPrefixFlags

   declare git_remote="${cmdarg_cfg['git-remote']}"
   declare branches="${cmdarg_cfg['branches']}"
   declare svn_remote="${cmdarg_cfg['svn-remote']}"
   declare svn_repository="${cmdarg_cfg['svn-repository']}"
   declare svn_project="${cmdarg_cfg['svn-project']}"
   declare fetch_each_branch="$(if [[ ${cmdarg_cfg['fetch-each-branch']} == 'true' ]] ; then echo 1 ; else echo 0 ; fi)"

   # Define branch prefix
   declare branch_prefix=""
   declare branchWithPrefix=0
   if [[ ! -z "${svn_repository}" ]] ; then
      branch_prefix="svn/$(branchPrefix ${svn_repository} ${svn_project})"
      branchWithPrefix=1
   fi

   # Fetch all SVN remote definitions at start
   action "Fetch from ${svn_remote}"
   git svn fetch --fetch-all && ok || failure "Unexpected exception!"

   # If branches are not explicitely specified, list remote branches filtered by prefix
   if [[ -z "${branches}" ]] ; then
      branches="$(lsBranches ${git_remote} ${branch_prefix})"
   fi

   # Iterate through specified branches
   action "Pull branches from ${git_remote} and push to ${svn_remote}"
   for branch in ${branches} ; do
      if [[ "${branch}" =~ ^svn\/.+$ ]] ; then
         # Construct local and remote branch names
         declare local_branch="${branch}"
         declare svn_branch="${svn_remote}/${branch}"
         declare git_branch="${git_remote}/${branch}"
         # Pull and push branch
         action "Pull ${git_branch} from ${git_remote} and push to ${svn_branch} on ${svn_remote}" "\e[4m\e[34m"
         pullBranchFromGitAndPushToSvn "${git_remote}" "${git_branch}" "${svn_remote}" "${svn_branch}" "${local_branch}" "${fetch_each_branch}" "${branchWithPrefix}" && ok "" "\e[4m\e[32m" || failure "Unexpected exception!" "\e[4m\e[31m"
      else
         echo "Ignoring branch ${branch} because branch name doesn't start with svn/"
      fi
   done
   ok "Sync finished."
}

validateBranchPrefixFlags()
{
   if [[ -z "${cmdarg_cfg['svn-repository']}" ]] && [[ ! -z "${cmdarg_cfg['svn-project']}" ]] ; then
      action "Validating arguments" && failure "svn-project flag can not be set without svn-repository flag."
      return 1
   fi
   if [[ ! -z "${cmdarg_cfg['svn-repository']}" ]] && [[ ! -z "${cmdarg_cfg['branches']}" ]] ; then
      action "Validating arguments" && failure "svn-repository flag can not be set with branches flag."
      return 1
   fi
   if [[ -z "${cmdarg_cfg['svn-repository']}" ]] && [[ -z "${cmdarg_cfg['branches']}" ]] ; then
      action "Validating arguments" && failure "Either svn-repository or branches flag needs to be set."
      return 1
   fi
}

# SVN -> GIT single branch sync
pullBranchFromSvnAndPushToGit()
{
   declare svn_remote="${1:?svn_remote argument missing}"
   declare svn_branch="${2:?svn_branch argument missing}"
   declare git_remote="${3:?git_remote argument missing}"
   declare git_branch="${4:?git_branch argument missing}"
   declare local_branch="${5:?local_branch argument missing}"
   declare fetch="${6:-1}"
   declare __prefix="[SVN -> GIT] "

   action "Pull ${svn_branch} into ${local_branch}"
   pullFromRemoteSvn "${svn_remote}" "${svn_branch}" "${local_branch}" "${fetch}" && ok || failure "Unexpected exception!"

   action "Push ${local_branch} to ${git_branch}"
   pushToRemote "${git_remote}" "${git_branch}" "${local_branch}" && ok || failure "Unexpected exception!"
}

# GIT -> SVN single branch sync
pullBranchFromGitAndPushToSvn()
{
   declare git_remote="${1:?git_remote argument missing}"
   declare git_branch="${2:?git_branch argument missing}"
   declare svn_remote="${3:?svn_remote argument missing}"
   declare svn_branch="${4:?svn_branch argument missing}"
   declare local_branch="${5:?local_branch argument missing}"
   declare fetch="${6:-1}"
   declare branchWithPrefix="${7:-0}"
   declare __prefix="[GIT -> SVN] "

   action "Pull ${git_branch} into ${local_branch}"
   pullFromRemote "${git_remote}" "${git_branch}" "${local_branch}" && ok || failure "Unexpected exception!"

   action "Push ${local_branch} to ${svn_branch}"
   pushToRemoteSvn "${svn_remote}" "${svn_branch}" "${local_branch}" "${fetch}" "${branchWithPrefix}" && ok || failure "Unexpected exception!"

   # We have one new commit (our own commit) in SVN now and we need to pull it back
   pullBranchFromSvnAndPushToGit "${svn_remote}" "${svn_branch}" "${git_remote}" "${git_branch}" "${local_branch}" "0"
}

pullFromRemoteSvn()
{
   declare svn_remote="${1:?svn_remote argument missing}"
   declare svn_branch="${2:?svn_branch argument missing}"
   declare local_branch="${3:?local_branch argument missing}"
   declare fetch="${4:-0}"

   # Fetch SVN remote definitions for given branch
   if ((${fetch})) ; then
      action "Fetch from ${svn_remote}"
      git svn fetch --fetch-all && ok || failure "Unexpected exception!"
   fi

   # Get last commit from SVN remote tracking branch
   action "Check HEAD for ${svn_branch}"
   branchHead "${svn_branch}" && ok || failure "Branch ${svn_branch} unknown!"

   # Get last commit from local branch
   action "Check HEAD for ${local_branch}"
   branchHead "${local_branch}" && ok || (warning "Branch ${local_branch} unknown!" && createLocalBranch "${local_branch}" "${svn_branch}")

   # If branches diverged
   action "Merging ${svn_branch} into ${local_branch}"
   if [ x"$(branchHead ${local_branch})" != x"$(branchHead ${svn_branch})" ] ; then
      mergeBranch "${svn_branch}" "${local_branch}" && ok "Changes successfully merged." || failure "Unexpected exception!"
   # If branches are in sync
   else
      info "Merge not needed, branches are in sync."
   fi
}

pullFromRemote()
{
   declare remote="${1:?remote argument undefined}"
   declare remote_branch="${2:?remote_branch argument undefined}"
   declare local_branch="${3:?local_branch argument undefined}"

   # Fetch from remote GIT repo
   action "Fetch from ${remote}"
   fetchBranch "${remote}" "${local_branch}" && ok || failure "Unexpected exception!"

   # Get last commit from remote tracking branch
   action "Check HEAD for ${remote_branch}"
   branchHead "${remote_branch}" && ok || failure "Branch ${remote_branch} unknown!"

   # Get last commit from local branch
   action "Check HEAD for ${local_branch}"
   branchHead "${local_branch}" && ok || (warning "Branch ${local_branch} unknown!" && createLocalBranch "${local_branch}" "${remote_branch}")

   # Pull from remote GIT repo if local and remote tracking branch heads are not on the same commit
   action "Merge ${remote_branch} into ${local_branch}"
   if [[ x"$(branchHead ${local_branch})" != x"$(branchHead ${remote_branch})" ]] ; then
      mergeBranch "${remote_branch}" "${local_branch}" && ok "Changes successfully merged." || failure "Unexpected exception!"
   else
      info "Merge not needed, branches are in sync."
   fi
}

pushToRemote()
{
   declare remote="${1:?remote argument undefined}"
   declare remote_branch="${2:?remote_branch argument undefined}"
   declare local_branch="${3:?local_branch argument undefined}"

   # Get remote url
   declare remote_url="$(git config --get remote.${remote}.url)"

   # Get last commit from local branch
   action "Check HEAD for ${local_branch}"
   branchHead "${local_branch}" && ok || failure "Branch ${local_branch} unknown!"

   # Get last commit from remote tracking branch
   action "Check HEAD for ${remote_branch}"
   branchHead "${remote_branch}" && ok || (warning "Branch ${remote_branch} unknown!" && createRemoteBranch "${remote}" "${remote_url}" "${local_branch}")

   # Push to remote GIT repo if local and remote tracking branch heads are not on the same commit
   action "Push ${local_branch} to ${remote}"
   if [[ x"$(branchHead ${local_branch})" != x"$(branchHead ${remote_branch})" ]] ; then
      (fetchAndMerge "${remote}" "${local_branch}" && pushBranch "${remote}" "${local_branch}") && ok "Changes sucessfully pushed." || failure "Unexpected exception!"
   else
      info "Push not needed, no changes found."
   fi
}

pushToRemoteSvn()
{
   declare remote="${1:-unknown}"
   declare remote_branch="${2:-unknown}"
   declare local_branch="${3:-unknown}"
   declare fetch="${4:-0}"
   declare branchWithPrefix="${5:-0}"

   # Branch on SVN doesn't have prefix svn-repo/svn/ nor repository/project/ prefix
   declare svn_branch="$(branchLeftStrip ${remote_branch} 2)"
   if (($branchWithPrefix)) ; then
      declare svn_branch="$(branchLeftStrip ${remote_branch} 2)"
   fi

   # Fetch SVN remote definitions for given branch
   if ((${fetch})) ; then
      action "Fetch from ${remote}"
      git svn fetch --fetch-all && ok || failure "Unexpected exception!"
   fi

   # Get last commit from local branch
   action "Check HEAD for ${local_branch}"
   branchHead "${local_branch}" && ok || failure "Branch ${local_branch} unknown!"

   # Get last commit from remote tracking branch
   action "Check HEAD for ${remote_branch}"
   branchHead "${remote_branch}" && ok || (warning "Branch ${remote_branch} unknown!" && createRemoteBranchSvn "${svn_branch}" "$(branchRightStrip ${local_branch} 1)/trunk" "${remote}" "${remote_branch}" '[GSB-1] - Creating branch from GIT')

   # Push to remote SVN repo if local and remote tracking branch heads are not on the same commit
   action "Push ${local_branch} to ${remote}"
   if [ x"$(branchHead ${remote_branch})" != x"$(branchHead ${local_branch})" ] ; then

      # Checkout SVN remote tracking branch
      checkoutBranch "${remote_branch}" || failure "Unexpected exception!"

      # Merge local branch to anonymous branch (detached head mode)
      action "Merge ${local_branch} to ${remote_branch}"
      git merge --log --no-ff "${local_branch}" && ok || failure

      # Commit changes to SVN
      action "Commit ${remote_branch} changes to SVN..."
      git svn dcommit && ok || failure

      ok "Changes sucessfully pushed."
   else
      info "Push not needed, no changes found."
   fi
}

createLocalBranch()
{
   declare local_branch="${1:?local_branch argument missing}"
   declare remote_branch="${2:?remote_branch argument missing}"
   # Create local branch starting from remote tracking branch head
   action "Creating branch ${local_branch} from ${remote_branch}"
   git branch "${local_branch}" "${remote_branch}" && ok || failure
   # Checkout local branch
   checkoutBranch "${local_branch}" || failure "Unexpected exception!"
   # Get head for local branch
   head=$(branchHead "${local_branch}")
   echo "${head}"
}

createRemoteBranch()
{
   declare remote="${1:?remote argument missing}"
   declare remote_url="${2:?remote url argument missing}"
   declare branch="${3:?branch argument missing}"
   # Create local branch on remote repo
   git push "${remote_url}" "refs/heads/${branch}:refs/heads/${branch}" || failure "Unexpected exception!"
   # Fetch and track remote branch
   fetchAndTrack "${remote}" "${branch}"
   # get head for remote tracking branch
   head=$(branchHead "${remote}/${branch}")
   echo "${head}"
}

createRemoteBranchSvn()
{
   declare svn_branch="${1:?svn branch name argument missing}"
   declare local_branch="${2:?local branch name missing}"
   declare remote="${3:?remote argument missing}"
   declare remote_branch="${4:?remote branch argument missing}"
   declare message="${5:?message argument missing}"
   # Checkout local branch
   checkoutBranch "${local_branch}" || failure "Unexpected exception!"
   # Create local branch on remote repo
   git svn branch "${svn_branch}" -m "${message}" || failure "Unexpected exception!"
   # Create remote tracking branch in local repo
   git svn fetch --fetch-all || failure "Unexpected exception!"
   # get head for remote tracking branch
   head=$(branchHead "${remote}/${remote_branch}")
   echo "${head}"
}

# Get specified branch latest commit id
branchHead()
{
   declare branch="${1:?branch argument is undefined}"
   # Get last commit from branch
   declare head="$(git rev-parse --verify -q ${branch})"
   # If branch doesn't exist
   if [ -z "${head}" ] ; then
      return 1
   else
      echo "${head}"
   fi
}

# Get current branch name
currentBranch()
{
   echo "$(git rev-parse --abbrev-ref HEAD)"
}

# List branch(es) fully qualified name(s)
lsBranchFQN()
{
   declare remote="${1:-}"
   declare repository="${2:-}"
   declare project="${3:-}"
   declare branches="${4:-}"

   declare branch_prefix=""
   [[ ! -z "${repository}" ]] && branch_prefix="${repository}/"
   [[ ! -z "${project}" ]] && branch_prefix="${branch_prefix}${project}/"

   # If no branches are defined, list all SVN's remote tracking branches
   if [ -z "${branches}" ] ; then
      branches=$(git branch -r --list "${remote}/${branch_prefix}*" | awk -F '/' '!/tags\// { for(i=2; i<=NF; i++) printf "%s",$i (i==NF?ORS:FS) }' | xargs)
   # Else add branch path to each branch name to construct FQN branch names
   else
      branches=$(for branch in $branches ; do echo "${branch_prefix}${branch}" ; done | xargs)
   fi

   echo "${branches}"
}

# List branches from remote definition
lsBranches()
{
   declare remote="${1:?remote argument missing}"
   declare prefix="${2:-}"
   declare stripFromStart="${3:-1}"
   # List branches by prefix and strip N path tokens from start
   declare branches=$(git branch -r --list "${remote}/${prefix}*" | awk -F '/' '!/tags\// { for(i=startFrom; i<=NF; i++) printf "%s",$i (i==NF?ORS:FS) }' startFrom=$((stripFromStart+1)) | xargs)
   echo "${branches}"
}

prependBranchesWithPrefix()
{
   declare branches="${1:?branches argument missing}"
   declare prefix="${2:-}"
   # Prepend branch by branch
   for branch in ${branches} ; do
      echo "$(prependBranchWithPrefix ${branch} ${prefix})"
   done
}

prependBranchWithPrefix()
{
   declare branch="${1:?branch argument missing}"
   declare prefix="${2:-}"
   # Return branch name prefixed with repository and project name
   echo "${prefix}${branch}"
}

branchPrefix()
{
   declare repository="${1:-}"
   declare project="${2:-}"
   # Return branch prefix as ${repository}/${project}/
   # respecting posibility that either repository or project variables are empty
   echo "${repository}${repository:+/}${project}${project:+/}"
}

branchLeftStrip()
{
   declare branch="${1:?branch argument missing}"
   declare stripFromStart="${2:-0}"
   echo "$(echo ${branch} | awk -F '/' '!/tags\// { for(i=startFrom; i<=NF; i++) printf "%s",$i (i==NF?ORS:FS) }' startFrom=$((stripFromStart+1)))"
}

branchRightStrip()
{
   declare branch="${1:?branch argument missing}"
   declare stripFromEnd="${2:-0}"
   echo "$(echo ${branch} | awk -F '/' '!/tags\// { for(i=1; i<=NF-stripFromEnd; i++) printf "%s",$i (i==NF-stripFromEnd?ORS:FS) }' stripFromEnd=$((stripFromEnd)))"
}

fetchAndTrack()
{
   declare remote="${1:?remote argument missing}"
   declare branch="${2:?branch argument missing}"
   (fetchBranch "${remote}" "${branch}" && trackBranch "${remote}" "${branch}" "${branch}") && return 0 || return 1
}

fetchAndMerge()
{
   declare remote="${1:?remote argument missing}"
   declare branch="${2:?branch argument missing}"
   (fetchBranch "${remote}" "${branch}" && mergeBranch "${remote}/${branch}" "${branch}") && return 0 || return 1
}

fetchBranch()
{
   declare remote="${1:?remote argument missing}"
   declare branch="${2:?branch argument missing}"
   # Create remote tracking branch
   git fetch "${remote}" "refs/heads/${branch}:refs/remotes/${remote}/${branch}" && return 0 || return 1
}

trackBranch()
{
   declare remote="${1:?remote argument missing}"
   declare remote_branch="${2:?remote branch argument missing}"
   declare local_branch="${3:?local branch argument missing}"
   # Create local tracking branch
   git branch -u "${remote}/${remote_branch}" "${local_branch}" && return 0 || return 1
}

pullBranch()
{
   declare remote="${1:?remote argument missing}"
   declare branch="${2:?branch argument missing}"
   # Create remote tracking branch
   git pull "${remote}" "refs/heads/${branch}:refs/remotes/${remote}/${branch}" && return 0 || return 1
}

pushBranch()
{
   declare remote="${1:?remote argument missing}"
   declare branch="${2:?branch argument missing}"
   # Create remote tracking branch
   git push "${remote}" "refs/heads/${branch}:refs/heads/${branch}" && return 0 || return 1
}

mergeBranch()
{
   declare from_branch="${1:?from branch argument missing}"
   declare into_branch="${2:?into into argument missing}"
   (checkoutBranch "${into_branch}" && git merge "${from_branch}") && return 0 || return 1
}

checkoutBranch()
{
   declare branch="${1}"
   # If branch is empty string, something very bad happened and we shoud exit immediately
   if [[ -z "${branch}" ]] ; then
      exit 1
   fi
   # Do checkout if branch is defined and it is not the same as the current branch
   if [[ ! -z "${branch}" ]] && [[ x"$(currentBranch)" != x"${branch}" ]] ; then
      # Checkout branch event if we have uncommited changes in current workspace
      git checkout -f "${branch}" || failure "Unexpected exception!"
      # Reset workspace tracked files to checked out branch head state
      git reset --hard || failure "Unexpected exception!"
      # Remove untracked files from workspace
      git clean -fx || failure "Unexpected exception!"
   fi
}

