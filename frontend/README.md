# GridGame
is a frontend service allows user interact with grid of particular size of N X N matrix by connecting to GridGame backend service over REST API calls.

### Business Requirements:
> User Story 1:  
Given : User like to interact with grid.  
When  : Application boots up.  
Then  : N X N Grid should be created by consuming POST API call by providing size details and load the initial grid.

>User Story 2:  
Given : Grid of size N X N created and rendered.  
When  : User clicks on a particular cell.  
Then  : Should make a request to PUT API call and update the grid as per the response.  
Should highlight the updated cells with a yellow background for a span of time.
Should highlight the sequence(fibonacci) cells with a green background for a span of time.

>User Story 3:   
Given : User like to interact with grid.
When  : Backend application failed to respond to any call.   
Then  : Should show error with the appropriate message using ngx-toaster library for a span of time.  


### Tech-Stack:
    Angular 19
    ts-mokita
    Karma
    Typescript
    Docker

### To boot up application:

```
ng serve
```

Once the application is running, open your browser and navigate to `http://localhost:4200/`.

### To build the project run:

```
ng build
```

## Running unit tests

To execute unit tests with the [Karma](https://karma-runner.github.io) test runner, use the following command:

```
ng test
```

### Angular components:
GirdComponent
    
    Grid component is responsible to combine all the cells together into its defined position
    so user can have a clear view and representation of a grid. Grid component intracts with backend 
    service by GeridService using httpcore library, request and responses are parsed and render accordingly.
    GridComponent intermideated the events between CellComponent and backed/Gerid service.

CellComponent
    
    Cell component is the core building block of grid, where cell will hold information of row, colument and value 
    to be displayed, meanwhile based on the value updated cell are blinked with yellow/green background for a specifice
    interval of time and also emits an event on click of the cell to update th corresponding grid row and column as per 
    ther business logic.

Environment
    
    Environemnt file created to hold environemnt specific variables to make it more flexible to update
    any necessary constants and less prone to manul erros on updating the same over the code.

### Implementation Details:
    1. Unit tests are added to cover the major logics flows, to make sure refactoring easier later part of stage.
    2. 3-Layered architecture Component, Model and services are created for clear differntation of logic that being built.
    3. Observalble - Scuscriber pattern followed as per the standards of Angular.
    4. Usage environemnt to make environemnt variables configurable easily.
    5. Used ngx-toster library for alrert/error messages.

### Assumptions :
    1. Grid size kept as constant over env file rather requesting from user, for sake of simpilicity.
    2. Provided a cell gets updated and also part of sequence, reflects green background considering sequence as the highest priority.
    3. Provided a cell being part of sqeuence, incremented value will not be shown rather it will be reset to 0 and get highlited with green background.

### Scope of Improvement:
    1. Micro frontend architecture over SPA.
    2. User Interaction test for EndToEnd testing.
    3. Better user interface/experience using bootstrap for responsiveness.
