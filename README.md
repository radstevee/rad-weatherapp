# rad-weatherapp

This is a rather simple weather app as my learning project for android development.

This app uses the OpenWeatherMap API to fetch weather data.
You have to sign up for the 5-day forecast API, which is free for up to 60 calls per minute.

[Sign up here](https://openweathermap.org/price)

Once you have your API key, put it in `app/src/main/assets/openweathermap_api_key.txt`.

## Installing

I am only using Android Studio for development, so please use that as well.

Development: Connect a phone and select it as the target device. Then click the green play button.

If you don't want to compile it yourself, check out
the [releases tab](https://github.com/radstevee/rad-weatherapp/releases).

## Features

- Forecast for the next 5 days
- You can use 209,579 cities around the world. In the autocomplete, there will only be 22,635 major
  cities due to performance reasons.