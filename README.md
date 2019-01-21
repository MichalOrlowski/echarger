# e-charger

## REST documentation

### Create price definition
`POST` `/prices`

#### Example request:
```json
{
     "pricePerMinute": "10",
     "durationFrom": "15:00",
     "durationTo": "19:00"
}
```

#### Responses:
`201` `Created`

`400` `Bad Request`

### Get price definition
`GET` `/prices/{id}`

#### Example response:
```json
{
"durationFrom": "15:00:00",
"durationTo": "19:00:00",
"pricePerMinute": 10
}
```

### Get charging price
`GET` `/prices/{customerId}/{chargingStart}/{chargingEnd}`

#### Parameters
`customerId` - customer identification number (example: 10)

`chargingStart` -  charging start date time (example: 1986-04-08T12:30:00)

`chargingEnd` -  charging end date time (example: 1986-04-08T12:30:00)

#### Responses:
`200` `OK`

`404` `Not found`

## Development guidelines
`gradle clean build` - build application (test and findbugs checks will be triggered)

`gradle bootRun` - start application (available under `localhost:8080`)

## Planned improvements
- Update/Delete endpoints for /prices -> It was not defined in the requirements, so I've decided to postpone it for the next phase
- PriceDefinition might have package-private scope. New DTO classes should be introduced in infra layer. I didn't to it yet, because it would be little bit over-engineering for this early stage
- Exception handling should be extended - right now 500 is returned, because there are no data input validators
- Repositories implementations are really dummy (e.g. customer)
- I was thinking about introducing facade and splitting PriceService into two smaller classes