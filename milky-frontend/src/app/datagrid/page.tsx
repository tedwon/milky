// http://localhost:3001/datagrid

// MUI Data Grid references
// https://mui.com/x/react-data-grid/#mit-version-free-forever
// https://mui.com/x/react-data-grid/getting-started/

"use client"
import * as React from 'react';
import Box from '@mui/material/Box';
import {DataGrid, GridColDef, GridValueGetterParams} from '@mui/x-data-grid';
import Container from '@mui/material/Container';
import useMediaQuery from '@mui/material/useMediaQuery';
import {createTheme, ThemeProvider} from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';

const columns: GridColDef[] = [
    {field: 'id', headerName: 'ID', width: 90},
    {
        field: 'firstName',
        headerName: 'First name',
        width: 150,
        editable: true,
    },
    {
        field: 'lastName',
        headerName: 'Last name',
        width: 150,
        editable: true,
    },
    {
        field: 'age',
        headerName: 'Age',
        type: 'number',
        width: 110,
        editable: true,
    },
    {
        field: 'fullName',
        headerName: 'Full name',
        description: 'This column has a value getter and is not sortable.',
        sortable: false,
        width: 160,
        valueGetter: (params: GridValueGetterParams) =>
            `${params.row.firstName || ''} ${params.row.lastName || ''}`,
    },
];

const rows = [
    {id: 1, lastName: 'Snow', firstName: 'Jon', age: 14},
    {id: 2, lastName: 'Lannister', firstName: 'Cersei', age: 31},
    {id: 3, lastName: 'Lannister', firstName: 'Jaime', age: 31},
    {id: 4, lastName: 'Stark', firstName: 'Arya', age: 11},
    {id: 5, lastName: 'Targaryen', firstName: 'Daenerys', age: null},
    {id: 6, lastName: 'Melisandre', firstName: null, age: 150},
    {id: 7, lastName: 'Clifford', firstName: 'Ferrara', age: 44},
    {id: 8, lastName: 'Frances', firstName: 'Rossini', age: 36},
    {id: 9, lastName: 'Roxie', firstName: 'Harvey', age: 65},
];

export default function DataGridDemo() {

    // Dark mode by System preference
    // https://mui.com/material-ui/customization/dark-mode/#system-preference
    const prefersDarkMode = useMediaQuery('(prefers-color-scheme: dark)');

    const theme = React.useMemo(
        () =>
            createTheme({
                palette: {
                    mode: prefersDarkMode ? 'dark' : 'light',
                },
            }),
        [prefersDarkMode],
    );

    return (
        <ThemeProvider theme={theme}>
            <CssBaseline/>
            <div>
                <Container maxWidth={"xl"}>
                    <Box sx={{height: 400, width: '100%'}}>
                        <DataGrid
                            rows={rows}
                            columns={columns}
                            initialState={{
                                pagination: {
                                    paginationModel: {
                                        pageSize: 5,
                                    },
                                },
                            }}
                            pageSizeOptions={[5]}
                            checkboxSelection
                            disableRowSelectionOnClick
                        />
                    </Box>
                </Container>
            </div>
        </ThemeProvider>
    );
}
