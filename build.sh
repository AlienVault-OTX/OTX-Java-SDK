set -e

if [ ! -z "${TRAVIS_TAG:-}" ]; then
  mvn versions:set -DnewVersion="${TRAVIS_TAG#v}"
fi

mvn install 
