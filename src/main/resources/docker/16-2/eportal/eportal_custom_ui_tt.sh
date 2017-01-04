resource_folder="./eportal-customizations/TalkTalk - Single Page (16.1.1)"

git clone https://github.nominum.com/sglukhov/eportal-customizations.git
echo 'Git clone is complete'

docker cp "$resource_folder"/translations/.     compose_eportal_1:/usr/local/nom/etc/nom-tomcat-eportal/webapps/single-page/singlePage/app/translations
echo 'Translations were updated'

docker cp "$resource_folder"/styles/.           compose_eportal_1:/usr/local/nom/etc/nom-tomcat-eportal/webapps/single-page/singlePage/styles/css
echo 'Styles were updated'

docker cp "$resource_folder"/talktalk.html  compose_eportal_1:usr/local/nom/etc/nom-tomcat-eportal/webapps/single-page

docker cp "$resource_folder"/images/.       compose_eportal_1:/usr/local/nom/etc/nom-tomcat-eportal/webapps/single-page/singlePage/img/
echo 'Images were updated'

rm -rf ./eportal-customizations
echo 'Temporary folder was deleted'

