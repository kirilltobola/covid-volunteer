# Covid-volunteer

![Logo!](/app/src/main/res/drawable/ic_launcher1_adaptive_fore.png)

## Description

Covid-volunteer is a mobile application, that helps connect medics and volunteers.

Medics can create orders, and volunteers can check this orders and respond to them.

## Requirements

- java 8+
- configured and launched [API application](https://github.com/kirilltobola/volunteer-api)

## How to install and run

First you have to launch [API application](https://github.com/kirilltobola/volunteer-api) and specify it's url adress in `public.properties` file:

```
echo API_URL=\"{URL}\" > public.properties
```

For example, if you launched API on your local machine on port `8080`, then `public.properties` should contain this code:

```
API_URL=10.0.2.2:8080
```

Then you have to create file `secure.properties` and put in there your [Google maps api](https://console.cloud.google.com/projectselector2/google/maps-apis) key:

```
echo MAPS_API_KEY=\"{YOUR_API_KEY}\" > secure.properties
```

## How to use

TODO
