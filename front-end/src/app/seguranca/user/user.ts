export interface User {
    id: string;
    name: string;
    status: boolean;
    username: string;
    cpf: string;
    phoneNumber: string;
    roleList: Role[];
}

export interface Role {
    authority: string;
    id: string;
    roleName: string;
}