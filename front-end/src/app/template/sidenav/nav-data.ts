import { INavbarData } from "./helper";

export const navbarData: INavbarData[] = [
    {
        routeLink: 'home',
        icon: 'fad fa-home',
        label: 'Home'
    },
    {
        routeLink: 'products',
        icon: 'fad fa-analytics',
        label: 'Dashboard'
    },
    {
        routeLink: 'tecnicos/list',
        icon: 'fad fa-money-check-edit-alt',
        label: 'Financeiro'
    },
    {
        routeLink: 'statistics',
        icon: 'fad fa-chart-line',
        label: 'Investimento'
    },
    {
        routeLink: 'coupens',
        icon: 'fal fa-list-alt',
        label: 'Serviços',
        items: [
            {
                routeLink: 'coupens/list',
                label: 'Aberto / Andamento'
            },
            {
                routeLink: 'coupens/create',
                label: 'Encerradas'
            }
        ]
    },
    {
        routeLink: 'settings',
        icon: 'fal fa-cog',
        label: 'Configuração',
        expanded: true,
        items: [
            {
                routeLink: 'settings/profile',
                label: 'Profile'
            },
            {
                routeLink: 'settings/customize',
                label: 'Customize'
            }
        ]
    },
];