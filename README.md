# Valiutchik

[![Android CI](https://github.com/fobo66/valiutchik-android/actions/workflows/android.yml/badge.svg)](https://github.com/fobo66/valiutchik-android/actions/workflows/android.yml)

This is an Android app for finding best exchange rates in Belarus. Available
on [Google Play](https://play.google.com/store/apps/details?id=fobo66.exchangecourcesbelarus)

App allows to quickly load the best exchange rates in some cities of Belarus. By clicking on list
item users can find nearest bank offices with best rates on the map. By long pressing on the
currency card users can copy currency rate to clipboard, e.g. for pasting into the calculator.
**Please pay attention that info about exchange rates may be out of date. Please clarify in banks.**
Info about exchange rates provided by [MyFin](https://myfin.by).

This is a small project of mine aimed to be the polygon for the newest and shiniest stuff in
Android. It features 100% Kotlin, Koin, coroutines, Clean Architecture, Room, AndroidX Benchmark and
unit tests. Any contributions are welcome.

Project's kanban board with plans and work in progress can be viewed [here](https://trello.com/b/0nMEURAL/valiutchik)

It was my first Android app that I've started in 2015, but I've changed repos in the wrong way, and
a lot of git history is missing. Plus there were a lot of refactorings, so this is not an app it was
back then. It wasn't public because it was quite ugly and it had leaked signing keys in git.

## Setup

For local development, you can set up environment variables via `.env` file. To get started:

```bash
cp .env.sample .env
```
Replace placeholders with actual values when you need to.
