# Grid Game 
  is a backend service allows frontend/API consumer application to handle basic functionality to Create, View, Update and clear the grid.Grid is build using N X N cells, each cell aware about it's position denoted by Row, Column and also it's Value(numeric).

### Business Requirements:
> User Story 1:  
    Given : User/Application likes to create a grid.  
    When  : Application with the request to create a grid of particular size.  
    Then  : N X N Grid should be created based on the request by initializing all its cell values to value Zero(0).

>User Story 2:  
    Given : Grid of size N X N created.  
    When  : User clicks on a particular cell.  
    Then  : Should update corresponding row and column by incrementing it's value by 1.  
            Incremented cell should be highlighted by blinking yellow background.  

>User Story 3:   
    Given : Grid of size N X N created.  
    When  : User clicks on a particular cell and rows and columns get incremented by 1.  
    Then  : Validate row and column for fibonacci sequence of particular length and reset cell value to Zero.  
            Reset cell should be highlighted by blinking green background.  

### Tech-Stack:
    Java 17
    Junit5
    Jakarta validation
    Lombok
    Gradle
    Jacoco
    Docker

### Clean and build the project
    ./gradlew clean build

### Run the application
    ./gradlew bRu


### APIs provided:

To Create Grid of specified size:

    curl --location 'http://localhost:8080/api/v1/grid' \  
    --header 'Content-Type: application/json' \
    --data '{
    "size": "2"
    }'


    Response: 201 CREATED with grid details.
    [
        [
            {
                "row": 0,
                "column": 0,
                "value": 0
            },
            {
                "row": 0,
                "column": 1,
                "value": 0
            }
        ],
        [
            {
                "row": 1,
                "column": 0,
                "value": 0
            },
            {
                "row": 1,
                "column": 1,
                "value": 0
            }
        ]   
    ]


    Incase of invalid request body: 400 BAD REQUEST thrown with response body {size=size should be positive integer number} 

To Update Grid by clicking on particular cell:
    
    curl --location --request PUT 'http://localhost:8080/api/v1/grid' \
    --header 'Content-Type: application/json' \
    --data '{
    "row":1,
    "column":1
    }'
    
    Response: 200 OK with updated grid details, Updated cell information and reset cell information
    
    {
        "grid": [
            [
                {
                    "row": 0,
                    "column": 0,
                    "value": 0
                },
                {
                    "row": 0,
                    "column": 1,
                    "value": 1
                }
            ],
            [
                {
                    "row": 1,
                    "column": 0,
                    "value": 1
                },
                {
                    "row": 1,
                    "column": 1,
                    "value": 1
                }
            ]
        ],
        "updatedCell": [
            {
                "row": 1,
                "column": 0,
                "value": 1
            },
            {
                "row": 1,
                "column": 1,
                "value": 1
            },
            {
                "row": 0,
                "column": 1,
                "value": 1
            }
        ],
        "resetCell": []
    }
    
    Incase of invalid cell(row/column) as part of request body, 400 BAD REQUEST throws with response body Grid row/column is out of bound.

To Reset Grid:

    curl --location --request PUT 'http://localhost:8080/api/v1/grid' \
    --header 'x-type: reset' \
    --header 'Content-Type: application/json' \
    --data '{
    "row":1,
    "column":1
    }'
    
    Response: 200 OK with updated grid details, Updated cell information and reset cell information.
    {
        "grid": [
            [
                {
                    "row": 0,
                    "column": 0,
                    "value": 0
                },
                {
                    "row": 0,
                    "column": 1,
                    "value": 0
                }
            ],
            [
                {
                    "row": 1,
                    "column": 0,
                    "value": 0
                },
                {
                    "row": 1,
                    "column": 1,
                    "value": 0
                }
            ]
        ],
        "updatedCell": [],
        "resetCell": []
    }

### Implementation Details:
    1. Test Driven Development approach:
        Implemented using TDD, where tests are written first to have clear scope of units/functions which 
        enables to build tests as documentation and modular code.
    2. REST API standards:
        APIs are built to REST API standard of LEVEL 3 by using appropriate HTTP verbs with resource based API path and response with 
        appropriate response code.
    3. SOLID principle:
        Followed to SOLID principles to build more maintainable, extendable and adoptable code and clear defined scope
        for each class of code.
    4. Chain of responsibility design pattern:
        Update handlers(RowIncrementHandler and ColumnIncrementHandler) are built chain of action pattern where series 
        of operation that needs to be applied over a grid, addition/removal/modification of functionality is much easier
        to enhance the grid update.
    5. Strategy Pattern:
        Sequence validators(Row and Column sequence validator) are built using strategy pattern where new strategy can be built and adopted on series of validations
        could easily extend to diagonal sequence validator or any such validator.
    6. Validator Has a Detector:
        To abstract out the validator and detector logic, both are build independently and injected as dependency, so validator just look into the reading grid pattern
        an actual sequence detection will be carried out by sequence detector.
    7. 3-Layered Architecture pattern: 
        Spring Model, Controller and Service pattern been used to have clear differentiate between business logic, input validation and 
        centralised error handling using Controller advice(where ever it is necessary).
    8. Sequence length made configurable:
        Sequence length made configurable as part of application properties, to make is more ease for testing.

### Assumptions : 
    1. Application supports only 1 grid at a time.
    2. Provided the size is N, grid will be of N X N matrix.
    3. Provided user clicks on a cell, the intersecting cell will be incremented only once(as part of Row Increment but not as a part of Column Increment).
    4. Fibonacci sequence of 10000 are generated on application bootup to validate across the grid sequence.
    5. As part of Fibonacci Row sequence validation and reset, only right to left with increasing order sequence is considered.
    6. As part of Fibonacci Column sequence validation and reset, only top to bottom with increasing order sequence is considered.

### Scope of Improvement:
    1. Implementation of API documentation using Open API/ Swagger, for better understanding of API exposed by service and it's capabilities.
    2. E2E test suit which could act as regression test suit to assure the basic functionality of the service.
    3. Optimising FibonacciSequenceDetection logic, rather than generating series of fixed length, finding out optimised way to detect the sequence. 
    
    