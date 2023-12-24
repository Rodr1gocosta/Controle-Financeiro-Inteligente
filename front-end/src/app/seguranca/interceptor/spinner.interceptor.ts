import { Injectable } from '@angular/core';
import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';
import { ProgressSpinnerService } from 'src/app/shared/util/progress-spinner/progress-spinner.service';

@Injectable()
export class SpinnerInterceptor implements HttpInterceptor {
  constructor(private progressSpinnerService: ProgressSpinnerService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.progressSpinnerService.show();

    return next.handle(request).pipe(finalize(() => {
        this.progressSpinnerService.hide();
      })
    );
  }
}
