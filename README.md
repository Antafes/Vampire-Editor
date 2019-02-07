Vampire Editor
==============

This is a character generator for Vampires: Dark Ages.
The editor will be in English and German, but as of now some parts may only have English texts available. After the German version of the 20th anniversary edition is published, I certainly will add all German translations.

Development
-----------

The editor is created with Netbeans.
Especially some of the GUI parts are created with the help of the Netbeans GUI Builder. Please keep this in mind, if you want to change anything on the GUI.

This tool uses my MyXML library (https://github.com/Antafes/MyXML) for parsing and writing XML files.

Deployment
----------

If a new version should be released, adjust the version in the VERSION file first and commit it accordingly into the master branch.
The build pipeline will automatically check if the version has been changed and thus will create a new release.