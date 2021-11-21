releaseVersion=$1

git tag "$releaseVersion"
git push origin "$releaseVersion"
