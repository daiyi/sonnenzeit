A cute sunrise/sunset calculator, offline-friendly for your camping/tramping/nature needs <3

```bash
lein sass auto             # auto-recompile styles
lein clean                 # in a separate terminal
lein figwheel dev
```


see application state:
```
re-frame.db/app-db
```

build:
```
lein clean
lein sass once
lein cljsbuild once min
```

#### next up
* request geolocation (lat, lng) from browser
  * chrome doesn't allow geoloc requests from non-TLS sites ):
  * but it still doesn't work on e.g. neocities-hosted TLS site. why?? it worked briefly when I demoed it this afternoon wtf
  * pause this for now.
* first pass on cute ui
  * svg clock
  * mark sunrise and sunset times on clock
  * at least get the css to work nicely
* offline!
* second pass on cute ui
  * colours aligned
  * stars n sun
* more views: graphs n charts
* actually fix geolocation
