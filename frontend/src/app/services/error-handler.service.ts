import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { HttpErrorResponse } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService {
  constructor(private toastr: ToastrService) {}
  handleError(error: HttpErrorResponse, customMessage?: string): void {
    let errorMessage: string;

    if (customMessage) {
      errorMessage = customMessage;
    } else if (error.error instanceof ErrorEvent) {
      // Client-side error
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Server-side error
      errorMessage = error.error?.message || error.statusText || 'Unknown server error';
    }

    console.error('Error occurred:', error);
    this.toastr.error(errorMessage, `Error ${error.status || ''}`, {
      timeOut: 5000,
      closeButton: true,
      progressBar: true
    });
  }
}
