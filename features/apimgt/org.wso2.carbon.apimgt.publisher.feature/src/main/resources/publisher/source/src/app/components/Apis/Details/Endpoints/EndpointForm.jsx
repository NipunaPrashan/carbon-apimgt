/**
 * Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React from 'react';
import { Component } from 'react';
import { Grid, Paper, Typography, Divider } from '@material-ui/core';
import EndpointsSelector from './EndpointsSelector';
import PropTypes from 'prop-types';
import Input from '@material-ui/core/Input';
import InputLabel from '@material-ui/core/InputLabel';
import FormHelperText from '@material-ui/core/FormHelperText';
import FormControl from '@material-ui/core/FormControl';
import { withStyles, MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import TextField from '@material-ui/core/TextField';
import purple from '@material-ui/core/colors/purple';
import green from '@material-ui/core/colors/green';
import Button from '@material-ui/core/Button';
import { FormattedMessage } from 'react-intl';

const styles = theme => ({
    root: {
        flexGrow: 1,
    },
    container: {
        display: 'flex',
        flexWrap: 'wrap',
    },
    margin: {
        margin: theme.spacing.unit,
    },
    paper: {
        padding: theme.spacing.unit * 2,
        textAlign: 'center',
        color: theme.palette.text.secondary,
    },
    textField: {
        marginLeft: theme.spacing.unit,
        marginRight: theme.spacing.unit,

    },
    button: {
        margin: theme.spacing.unit,
    },
});

class EndpointForm extends Component {

    constructor(props) {
        super(props);
        this.state = {
            serviceUrl: '',
            timeout: '',

        };
        this.handleTextFieldChange = this.handleTextFieldChange.bind(this);
    }

    handleTextFieldChange(event) {
        const id = event.currentTarget.id;
        this.setState({ id: event.currentTarget.value });
    }

    render() {
        const { classes } = this.props;
        return (
            <Grid item xs={6}>

                <Paper className={classes.paper}>
                    <form className={classes.container} noValidate autoComplete="off">
                        <Grid item xs={6}>
                            <TextField
                                id="epType"
                                label={<FormattedMessage id="type" defaultMessage="Type" />}
                                className={classes.textField}
                                defaultValue={this.props.isInline ? 'Inline' : 'Global'}
                                fullWidth
                                margin="normal"
                                onChange={this.handleTextFieldChange}
                                disabled={this.props.readOnly}
                            />
                        </Grid>
                        <Grid item xs={6}>
                            <TextField
                                id="serviceUrl"
                                label={<FormattedMessage id="service.url" defaultMessage="Service URL" />}
                                className={classes.textField}
                                value={this.props.selectedEndpointConfig.url}
                                fullWidth
                                margin="normal"
                                disabled={this.props.readOnly}
                            />
                        </Grid>
                        <Grid item xs={6}>
                            <TextField
                                id="timeout"
                                label={<FormattedMessage id="timeout" defaultMessage="Timeout" />}
                                className={classes.textField}
                                value={this.props.selectedEndpointConfig.timeout}
                                fullWidth
                                margin="normal"
                                disabled={this.props.readOnly}
                            />
                        </Grid>
                    </form>
                </Paper>
                <Divider />
                <Paper className={classes.paper}>
                    <Grid item xs={6}>
                        <TextField
                            id="maxTPS"
                            label={<FormattedMessage id="max.tps" defaultMessage="Max TPS" />}
                            className={classes.textField}
                            value={this.props.endpoint.maxTps}
                            fullWidth
                            margin="normal"
                            disabled={this.props.readOnly}
                        />
                    </Grid>
                    <Grid item xs={6}>
                        <TextField
                            id="endpointType"
                            label={<FormattedMessage id="endpoint.type" defaultMessage="Endpoint Type" />}
                            className={classes.textField}
                            value={this.props.endpoint.type}
                            fullWidth
                            margin="normal"
                            disabled={this.props.readOnly}
                        />
                    </Grid>
                </Paper>
            </Grid>

        )
    }
}

EndpointForm.propTypes = {
    classes: PropTypes.object.isRequired,
};

export default withStyles(styles)(EndpointForm);