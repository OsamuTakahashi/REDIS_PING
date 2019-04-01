# REDIS_PING: jgroups discovery protocol using redis

REDIS_PING is a discovory protocol using redis server.
Its maily target is for cloud computing environment, like GCP, aws and so on.

Stability of discovery protocols using cloud storage strognly depends on its stability of service. I had a terrible experience from that. That is the reason why I wrote REIDS_PING.

## how to configure

Fill out your redis server information.

```
<org.sopranoworks.REDIS_PING
  redis_host="<redis host address:default = localhost>"
  redis_port="<redis service port:default = 6379>"
  redis_db="<using db no:default = 0>"
/>
```

## Licence

MIT
