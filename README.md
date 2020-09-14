# Harry-Kart
## Description
Harry-Kart in a special kind of horse racing.

The horses participating have a base speed, they can run with that speed indefinitely.
The track is a 1000 meters loop and is divided in lanes, each horse runs on a lane and every lane has the same length.   
The horses run the first loop at their base speed but at the end of each loop they find a power-up or power-down.

The power-ups/downs are numbers, negative or positive, representing how much the horse speeds up or slows down.

Your task is to compute the top 3 ranking.

## Example 1

### Input

**Number of loops:** 3

**Start List:**

| Lane | Horse name     | Base speed |
|------|----------------|------------|
| 1    | TIMETOBELUCKY  | 10         |
| 2    | CARGO DOOR     | 10         |
| 3    | HERCULES BOKO  | 10         |
| 4    | WAIKIKI SILVIO | 10         |

**Power-Ups/Downs:**

| Loop | Lane 1 | Lane 2 | Lane 3 | Lane 4 |
|------|--------|--------|--------|--------|
| 1    | 1      | 1      | 0      | -2     |
| 2    | 1      | -1     | 2      | -2     |

### Result

| Position | Horse Name    |
|----------|---------------|
| 1st      | TIMETOBELUCKY |
| 2nd      | HERCULES BOKO |
| 3rd      | CARGO DOOR    |


## Example 2

### Input

**Number of loops:** 3

**Start List:**

| Lane | Horse name     | Base speed |
|------|----------------|------------|
| 1    | TIMETOBELUCKY  | 10         |
| 2    | CARGO DOOR     | 10         |
| 3    | HERCULES BOKO  | 10         |
| 4    | WAIKIKI SILVIO | 10         |

**Power-Ups/Downs:**

| Loop | Lane 1 | Lane 2 | Lane 3 | Lane 4 |
|------|--------|--------|--------|--------|
| 1    | 0      | 0      | 1      | 3      |
| 2    | 10     | 0      | 0      | 1      |

### Result

| Position | Horse Name    |
|----------|---------------|
| 1st      | WAIKIKI SILVIO|
| 2nd      | TIMETOBELUCKY |
| 3rd      | HERCULES BOKO |

## Implementation
The assignment has to be implemented as a spring boot application with Java 8, here you find the boilerplate application.

The input is provided as an XML document (see examples ```/src/main/resources/input_0.xml``` and ```/src/main/resources/input_1.xml```),
in case you need it we provide the XML schema for it (```/src/main/resources/input.xsd```)

The output must be a json document of this form:
```json
{
   "ranking": [
      {"position": 1, "horse": "TIMETOBELUCKY"},
      {"position": 2, "horse": "HERCULES BOKO"},
      {"position": 3, "horse": "CARGO DOOR"}
   ]
}
```

The application we provide has a rest endpoint accepting XML and returning JSON (http://localhost:8080/api/play), you can use it as entry point.

When you are done zip the project (without the target folder) and send it back to us. You can leave the .git folder if you want.

## Example with curl

### Working example 1
```
curl -s  -X POST --header Content-Type:application/xml --data "@./src/main/resources/input_0.xml" localhost:8080/api/play |python -m json.tool
{
    "ranking": [
        {
            "horse": "TIMETOBELUCKY",
            "position": 1
        },
        {
            "horse": "HERCULES BOKO",
            "position": 2
        },
        {
            "horse": "CARGO DOOR",
            "position": 3
        }
    ]
}
```

### Example with illegal input data
```
curl -i -X POST --header Content-Type:application/xml --data "dsdsdds" localhost:8080/api/play 
HTTP/1.1 400 
Content-Type: text/plain;charset=UTF-8
Content-Length: 27
Date: Sat, 12 Sep 2020 14:13:54 GMT
Connection: close

Unable to parse input data
```
