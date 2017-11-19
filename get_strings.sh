#!/usr/bin/env bash
url="https://us-central1-project-5641153190345267944.cloudfunctions.net/i18nApi/"

echo "downloading list of enabled langs"
langs="$(curl "${url}")"

echo "doownloading langs"
curl --create-dirs -o "app/src/main/res/values-#1/strings.xml" "${url}{${langs}}"

mv "app/src/main/res/values-en/strings.xml" "app/src/main/res/values/strings.xml"
rmdir "app/src/main/res/values-en"

echo "all langs downloaded"
