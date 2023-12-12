import { Pipe, PipeTransform } from "@angular/core";

@Pipe({
    name: 'real'
})
export class RealPipe implements PipeTransform {
    transform(value: number): string {
        // Verifica se o valor é um número
        if (isNaN(value)) {
            return 'R$ 0,00';
        }

        // Formata o valor para reais
        const formattedValue = new Intl.NumberFormat('pt-BR', {
            style: 'currency',
            currency: 'BRL'
        }).format(value);

        return formattedValue;
    }
}