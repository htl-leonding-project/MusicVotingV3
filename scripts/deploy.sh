#webseite
cd /var/www

rm -rd addMusic.zip
rm -rd html

wget https://github.com/MusicVoting/MusicVotingV3/releases/latest/download/addMusic.zip
unzip addMusic.zip

mv addMusic html
rm -rd addMusic.zip

#server
cd /home/mvadmin/deployment

rm -rd quarkus-app
rm -rd backend.zip

wget https://github.com/MusicVoting/MusicVotingV3/releases/latest/download/backend.zip
unzip backend.zip

rm -rd backend.zip

cd quarkus-app
wget https://github.com/MusicVoting/MusicVotingV3/releases/latest/download/artists.csv
java -jar quarkus-run.jar






