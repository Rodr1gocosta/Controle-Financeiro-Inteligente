import { Categories } from "./categories";

export interface Planning {
    id?: string;
    month?: number;
    year?: number;
    totalPlanned?: number;
    categoriesRecords?: Categories[];
}