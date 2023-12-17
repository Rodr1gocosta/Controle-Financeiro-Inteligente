import { CategoryDefault } from "src/app/seguranca/admin/category-default/category-default";
import { Category } from "./category";

export interface Categories {
    id: string;
    category: CategoryDefault;
    descricao: string;
    planned: number;
    categoryRecord?: Category;

}