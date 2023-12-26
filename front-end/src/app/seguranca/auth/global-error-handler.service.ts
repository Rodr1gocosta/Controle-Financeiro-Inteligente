import { HttpErrorResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { MessageOperationService } from "src/app/shared/util/message-operation/message-operation.service";

@Injectable({
    providedIn: 'root',
})
export class GlobalErrorHandlerService {
    constructor(private messageOperationService: MessageOperationService) { }

    handleHttpError(error: HttpErrorResponse): void {
      let errorMessage!: string;
      
      if (error.error != null) {
          errorMessage = error.error.error;
        }

        switch (error.status) {
            case 400: {
              errorMessage = 'Solicitação inválida. Verifique os dados fornecidos.';
              break;
            }
            case 401: {
              errorMessage = 'Credenciais inválidas';
              break;
            }
            case 403: {
              errorMessage = 'Acesso negado. Você não tem permissão para acessar este recurso.';
              break;
            }
            case 404: {
              errorMessage = 'O recurso solicitado não foi encontrado.';
              break;
            }
            case 422: {
              errorMessage = 'Não foi possível processar a solicitação devido a problemas com os dados fornecidos.';
              break;
            }
            case 429: {
              errorMessage = 'Muitas solicitações. Tente novamente mais tarde.';
              break;
            }
            case 500: {
              errorMessage = 'Ocorreu um erro na operação. Por favor, tente novamente mais tarde.';
              break;
            }
            case 503: {
              errorMessage = 'Servidor web está temporariamente fora do ar devido a uma manutenção programada';
              break;
            }
            default: {
              errorMessage = 'Ocorreu um erro inesperado. Tente novamente mais tarde.';
              break;
            }
          }

        this.messageOperationService.message(errorMessage, 'error');
    }
}