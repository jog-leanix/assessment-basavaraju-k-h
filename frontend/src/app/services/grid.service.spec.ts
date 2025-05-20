import {TestBed} from '@angular/core/testing';
import {HttpTestingController} from '@angular/common/http/testing';
import {provideHttpClient} from '@angular/common/http';
import {provideHttpClientTesting} from '@angular/common/http/testing';
import {GridService} from './grid.service';
import {Cell} from '../models/Cell';
import {environment} from '../../environments/environment';
import {ToastrModule} from 'ngx-toastr';

describe('GridService', () => {
  let service: GridService;
  let httpMock: HttpTestingController;
  const apiUrl = `${environment.apiUrl}/grid`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        ToastrModule.forRoot()
      ],
      providers: [
        GridService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(GridService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getGrid', () => {
    it('should return a grid of cells', () => {
      const mockGrid: Cell[][] = [
        [{row: 0, column: 0, value: 1}, {row: 0, column: 1, value: 2}],
        [{row: 1, column: 0, value: 3}, {row: 1, column: 1, value: 4}]
      ];

      service.getGrid().subscribe(grid => {
        expect(grid).toEqual(mockGrid);
      });

      const req = httpMock.expectOne(apiUrl);
      expect(req.request.method).toBe('GET');
      req.flush(mockGrid);
    });

    it('should handle errors when getting the grid', () => {
      const errorMessage = 'Server error';

      service.getGrid().subscribe({
        next: () => fail('Expected an error, not data'),
        error: error => {
          expect(error.status).toBe(500);
          expect(error.statusText).toBe(errorMessage);
        }
      });

      const req = httpMock.expectOne(apiUrl);
      req.flush(null, {status: 500, statusText: errorMessage});
    });
  });

  describe('createNewGrid', () => {
    it('should create a new grid with the specified size', () => {
      const size = 3;
      const mockGrid: Cell[][] = [
        [{row: 0, column: 0, value: 1}, {row: 0, column: 1, value: 2}, {row: 0, column: 2, value: 3}],
        [{row: 1, column: 0, value: 4}, {row: 1, column: 1, value: 5}, {row: 1, column: 2, value: 6}],
        [{row: 2, column: 0, value: 7}, {row: 2, column: 1, value: 8}, {row: 2, column: 2, value: 9}]
      ];

      service.createNewGrid(size).subscribe(grid => {
        expect(grid).toEqual(mockGrid);
      });

      const req = httpMock.expectOne(apiUrl);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual({size});
      req.flush(mockGrid);
    });

    it('should handle errors when creating a new grid', () => {
      const size = 3;
      const errorMessage = 'Invalid size';

      service.createNewGrid(size).subscribe({
        next: () => fail('Expected an error, not data'),
        error: error => {
          expect(error.status).toBe(400);
          expect(error.statusText).toBe(errorMessage);
        }
      });

      const req = httpMock.expectOne(apiUrl);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual({size});
      req.flush(null, {status: 400, statusText: errorMessage});
    });
  });

  describe('clickCell', () => {
    it('should update the grid when a cell is clicked', () => {
      const row = 1;
      const column = 1;
      const updatedCell = {row: 1, column: 1, value: 5};
      const resetCell = {row: 0, column: 0, value: 1};

      const mockResponse = {
        grid: [
          [{row: 0, column: 0, value: 1}, {row: 0, column: 1, value: 2}],
          [{row: 1, column: 0, value: 3}, {row: 1, column: 1, value: 5}]
        ],
        updatedCell: new Set([updatedCell]),
        resetCell: new Set([resetCell])
      };

      service.clickCell(row, column).subscribe(response => {
        expect(response.grid).toEqual(mockResponse.grid);

        // Check if yellowCells contains the expected cell
        expect(response.yellowCells.size).toBe(1);
        expect(response.yellowCells.has(updatedCell)).toBeTruthy();

        // Check if greenCells contains the expected cell
        expect(response.greenCells.size).toBe(1);
        expect(response.greenCells.has(resetCell)).toBeTruthy();
      });

      const req = httpMock.expectOne(apiUrl);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual({row, column});
      req.flush(mockResponse);
    });

    it('should handle errors when clicking a cell', () => {
      const row = 1;
      const column = 1;
      const errorMessage = 'Invalid cell position';

      service.clickCell(row, column).subscribe({
        next: () => fail('Expected an error, not data'),
        error: error => {
          expect(error.status).toBe(400);
          expect(error.statusText).toBe(errorMessage);
        }
      });

      const req = httpMock.expectOne(apiUrl);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual({row, column});
      req.flush(null, {status: 400, statusText: errorMessage});
    });
  });
  describe('resetGrid', () => {
    const mockResponse = {
      grid: [
        [{row: 0, column: 0, value: 1}, {row: 0, column: 1, value: 2}],
        [{row: 1, column: 0, value: 3}, {row: 1, column: 1, value: 5}]
      ],
      updatedCell: new Set(),
      resetCell: new Set()
    };
    it("should reset the grid using put grid api request with necessary headers", () => {

      service.resetGrid().subscribe(() => {});

      const req = httpMock.expectOne(apiUrl);
      expect(req.request.method).toBe('PUT');
      expect(req.request.headers.get('x-type')).toBe('reset');
      req.flush(mockResponse, {status: 200, statusText: 'OK'});
    })
  });
});
