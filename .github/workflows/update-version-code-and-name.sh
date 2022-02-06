branch=$1
repo=$2
workspace=$3
releaseName=$4
githubToken=$5

git remote set-url --push origin "https://antonKozyriatskyi:$githubToken@github.com/$repo"
git fetch
git checkout -b "$branch" "origin/$branch"

kotlinc -script "${workspace}/.github/workflows/update-release-version.kts" -- --name "$releaseName" --workspace "$workspace"