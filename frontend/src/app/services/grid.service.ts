import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError, Observable, throwError} from 'rxjs';
import {map} from 'rxjs/operators';
import {Cell} from '../models/Cell';
import {environment} from '../../environments/environment';
import {ErrorHandlerService} from './error-handler.service';

@Injectable({
  providedIn: 'root',
})
export class GridService {
  private apiUrl = `${environment.apiUrl}/grid`;

  constructor(private http: HttpClient, private errorHandlerService: ErrorHandlerService) {
  }

  getGrid(): Observable<Cell[][]> {
    return this.http.get<Cell[][]>(this.apiUrl).pipe(
      catchError(error => {
        this.errorHandlerService.handleError(error, 'Failed to Load grid');
        return throwError(error);
      })
    );
  }

  createNewGrid(size: number): Observable<Cell[][]> {
    return this.http.post<Cell[][]>(`${this.apiUrl}`, {size}).pipe(
      catchError(error => {
        this.errorHandlerService.handleError(error, 'Failed to create grid');
        return throwError(error);
      }));
  }

  clickCell(row: number, column: number): Observable<{
    grid: Cell[][];
    yellowCells: Set<Cell>;
    greenCells: Set<Cell>;
  }> {
    return this.http.put<{
      grid: Cell[][];
      updatedCell: Set<Cell>;
      resetCell: Set<Cell>;
    }>(`${this.apiUrl}`, {row, column}).pipe(
      map(response => ({
        grid: response.grid,
        yellowCells: response.updatedCell,
        greenCells: response.resetCell
      })),
      catchError(error => {
        this.errorHandlerService.handleError(error, 'Failed to update grid');
        return throwError(error);
      })
    );
  }

  resetGrid(): Observable<{
    grid: Cell[][];
    yellowCells: Set<Cell>;
    greenCells: Set<Cell>;
  }> {
    return this.http.put<{
      grid: Cell[][];
      updatedCell: Set<Cell>;
      resetCell: Set<Cell>;
    }>(`${this.apiUrl}`,{}, {headers: new HttpHeaders({"x-type": "reset"})}).pipe(
      map(response => ({
        grid: response.grid,
        yellowCells: response.updatedCell,
        greenCells: response.resetCell
      })),
      catchError(error => {
        this.errorHandlerService.handleError(error, 'Failed to reset grid');
        return throwError(error);
      })
    );
  }
}
