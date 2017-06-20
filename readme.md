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
* first pass on cute ui
* offline!
* second pass on cute ui
  * colours
  * stars n sun
* more views: graphs n charts
