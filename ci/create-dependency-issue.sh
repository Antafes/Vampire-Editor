#!/bin/sh

#
# This file is part of Vampire Editor.
#
# Vampire Editor is free software: you can redistribute it and/or modify
# it under the terms of the GNU Lesser General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# Vampire Editor is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public License
# along with Vampire Editor. If not, see <http://www.gnu.org/licenses/>.
#
# @package Vampire Editor
# @author Marian Pollzien <map@wafriv.de>
# @copyright (c) 2026, Marian Pollzien
# @license https://www.gnu.org/licenses/lgpl.html LGPLv3
#

GITHUB_TOKEN="$1"
ROOT_FOLDER="$( pwd )/../"
OUTPUT_DIR="${ROOT_FOLDER}/target"
SUMMARY_FILE="${OUTPUT_DIR}/dependency-update-summary.json"
REPORT_FILE="${OUTPUT_DIR}/dependency-update-report.md"
ISSUE_TITLE_PREFIX="Dependency updates:"

GITHUB_OWNER="${GITHUB_OWNER:-Antafes}"
GITHUB_REPO="${GITHUB_REPO:-Vampire-Editor}"
GITHUB_API_BASE="https://api.github.com/repos/${GITHUB_OWNER}/${GITHUB_REPO}"

if [ -z "${GITHUB_TOKEN}" ]
then
    echo "Missing GitHub token."
    exit 1
fi

if [ ! -f "${SUMMARY_FILE}" ] || [ ! -f "${REPORT_FILE}" ]
then
    echo "Missing dependency scan artifacts."
    exit 1
fi

if ! grep -q '"hasUpdates": true' "${SUMMARY_FILE}"
then
    echo "No dependency updates found. Skipping issue automation."
    exit 0
fi

TIMESTAMP="$(date -u +%Y-%m-%dT%H:%M:%SZ)"
ISSUE_TITLE="${ISSUE_TITLE_PREFIX} ${TIMESTAMP}"

REPORT_CONTENT="$(cat "${REPORT_FILE}")"
COMMENT_BODY="Automated dependency scan found updates at ${TIMESTAMP}.\n\n${REPORT_CONTENT}"

# JSON escaping for API payloads.
ESCAPED_COMMENT_BODY="$(printf '%s' "${COMMENT_BODY}" | sed 's/\\/\\\\/g' | sed 's/"/\\"/g' | sed ':a;N;$!ba;s/\n/\\n/g')"
ESCAPED_ISSUE_BODY="$(printf '%s' "${REPORT_CONTENT}" | sed 's/\\/\\\\/g' | sed 's/"/\\"/g' | sed ':a;N;$!ba;s/\n/\\n/g')"

OPEN_ISSUES_RESPONSE="$(curl -s \
    -H "Accept: application/vnd.github+json" \
    -H "Authorization: Bearer ${GITHUB_TOKEN}" \
    "${GITHUB_API_BASE}/issues?state=open&labels=type:dependencies&per_page=10")"

EXISTING_ISSUE_NUMBER="$(printf '%s' "${OPEN_ISSUES_RESPONSE}" | grep -m1 '"number":' | sed 's/[^0-9]//g')"
EXISTING_ISSUE_TITLE="$(printf '%s' "${OPEN_ISSUES_RESPONSE}" | grep -m1 '"title":' | sed 's/.*"title": "//; s/",$//')"

if [ -n "${EXISTING_ISSUE_NUMBER}" ] && printf '%s' "${EXISTING_ISSUE_TITLE}" | grep -q "^${ISSUE_TITLE_PREFIX}"
then
    echo "Updating existing dependency issue #${EXISTING_ISSUE_NUMBER}."
    curl -s \
        -X POST \
        -H "Accept: application/vnd.github+json" \
        -H "Authorization: Bearer ${GITHUB_TOKEN}" \
        "${GITHUB_API_BASE}/issues/${EXISTING_ISSUE_NUMBER}/comments" \
        -d "{\"body\": \"${ESCAPED_COMMENT_BODY}\"}" > /dev/null
else
    echo "Creating new dependency issue."
    curl -s \
        -X POST \
        -H "Accept: application/vnd.github+json" \
        -H "Authorization: Bearer ${GITHUB_TOKEN}" \
        "${GITHUB_API_BASE}/issues" \
        -d "{\"title\": \"${ISSUE_TITLE}\", \"body\": \"${ESCAPED_ISSUE_BODY}\", \"labels\": [\"type:dependencies\", \"dependencies\"]}" > /dev/null
fi

echo "Dependency issue automation completed."
exit 0

