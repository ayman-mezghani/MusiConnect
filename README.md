[![Build Status](https://travis-ci.org/ayman-mezghani/MusiConnect.svg?branch=master)](https://travis-ci.org/ayman-mezghani/MusiConnect)
[![Build Status](https://api.cirrus-ci.com/github/ayman-mezghani/MusiConnect.svg)](https://cirrus-ci.com/github/ayman-mezghani/MusiConnect)
[![Maintainability](https://api.codeclimate.com/v1/badges/7d48f8b222b5301a9c7d/maintainability)](https://codeclimate.com/github/ayman-mezghani/MusiConnect/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/7d48f8b222b5301a9c7d/test_coverage)](https://codeclimate.com/github/ayman-mezghani/MusiConnect/test_coverage)

# SDP Proposal - Team \# 13

## Team / App name:
MusiConnect

## Description:
Brief description: An app for musicians to locate other musicians. An individual musician can search for another musician in order to form a band, or a band can search either for another musician or another band in order to create jam sessions.
- Displaying musician profiles
- Using a positionning system to locate the closest musicians and closest bands that are searching for musicians
- Local database and cloud database to store all musicians and bands (group of musicians)

## Requirements:
### Split app model:
Google cloud services => Store all information on users of the app and each user's video

### Sensor usage:
- GPS for locating the nearest musicians
- (Camera to record a performance (demo video) in order to add it to the profile)

### User support:
2 (maybe 3) types of users:
- User must be authenticated in order to use the application
- Individual musicians (that can only look for other musicians)
- Individual musician can have only 1 video (time limited e.g. 2 mins) and edit it if needed
- Band / group of musicians (that can look for musicians in order for them to join the group OR other groups)
- Each individual musician can be part of a group
- Eventually time permitting: Other users that can create an event in order for a band or 2 to apply

### Local cache:
- Store information of musicians
- Store videos if no internet connection

### Offline mode:
Can do all requests as if online, but submit changes only when connection works again
