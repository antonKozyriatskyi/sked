workspace=$1
releaseName=$2

git config --local user.name "GitHub CI"
git config --local user.email "ci@github.com"

git add "$workspace/app/build.gradle"
git commit -m "Update version to $releaseName"
git push origin