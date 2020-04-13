# Alexa Skill Sky
Playing around with Alexa and controlling Sky boxes (mostly SkyQ)

## What does this do so far?
* On `http://localhost:4390/health` it will report some basic health:
```
{"status":"UP"}
```
* On `http://localhost:4390/discover` it will report all devices it suspects are Sky devices we'll be able to control: 
```
[
    {"remotePort":49160,"restPort":9006,"ip":"192.168.0.2"},
    {"remotePort":49160,"restPort":9006,"ip":"192.168.0.3"}
]
```

## Sources of information and inspiration
* Sky box interaction
  * https://github.com/dalhundal
    * https://github.com/dalhundal/sky-remote-cli
    * https://github.com/dalhundal/sky-remote 
    * https://github.com/dalhundal/sky-q
  * https://github.com/ndg63276/alexa-sky-hd
  * https://bradwood.gitlab.io/pyskyq
    * https://gitlab.com/bradwood/pyskyq
  * https://gladdy.uk/blog/2017/03/13/skyq-upnp-rest-and-websocket-api-interfaces
* Alexa integration
  * https://github.com/ndg63276/alexa-sky-hd
  * https://www.hackster.io/nishit-patel/controlling-raspberry-pi-using-alexa-33715b
  * https://developer.amazon.com/en-US/alexa
    * https://developer.amazon.com/en-US/alexa/connected-devices/design
    * https://developer.amazon.com/en-US/alexa/connected-devices/alexa-connect-kit
    * https://developer.amazon.com/en-US/docs/alexa/smarthome/understand-the-smart-home-skill-api.html
* Other
  * IP Addresses
    * https://github.com/maltalex/ineter
  * Scanning
    * https://github.com/angryip/ipscan
    * https://rosettacode.org/wiki/Sockets#Kotlin
  * Timing operations
    * https://proandroiddev.com/measuring-execution-times-in-kotlin-460a0285e5ea