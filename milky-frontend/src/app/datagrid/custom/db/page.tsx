// http://localhost:3001/datagrid/custom/db

// MUI Data Grid references
// https://mui.com/x/react-data-grid/#mit-version-free-forever
// https://mui.com/x/react-data-grid/getting-started/

"use client"
import * as React from 'react';
import {useEffect, useState} from 'react';
import Box from '@mui/material/Box';
import {DataGrid, GridColDef} from '@mui/x-data-grid';
import Container from '@mui/material/Container';
import useMediaQuery from '@mui/material/useMediaQuery';
import {createTheme, ThemeProvider} from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';


type Team = {
    id: number;
    name: string;
};

const columns: GridColDef[] = [
    {field: 'id', headerName: 'ID', width: 90},
    {
        field: 'name',
        headerName: 'Name',
        width: 150,
        editable: true,
    },
];

export default function DataGridDemo() {

    const [teams, setTeams] = useState<Team[]>([]);

    // Retrieve All Offerings from the backend-server
    // Note: the empty deps array [] means
    // this useEffect will run once
    // similar to componentDidMount()
    // https://reactjs.org/docs/faq-ajax.html
    useEffect(() => {
        fetch("http://localhost:2403/milky/api/v1/team")
            .then(res => res.json())
            .then((dbTeams: Team[]) => {
                    setTeams(dbTeams);
                },
                // Note: it's important to handle errors here
                // instead of a catch() block so that we don't swallow
                // exceptions from actual bugs in components.
                error => {
                    console.log(error);
                }
            )
    }, [])

    const rows = teams;

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
