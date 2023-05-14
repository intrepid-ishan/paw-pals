// react
import React, { useState, useEffect, useContext } from 'react';
import { Link as RouterLink } from 'react-router-dom';

// material ui
import { Container, Link, IconButton, Typography } from '@material-ui/core';
import ArrowBackIosIcon from '@material-ui/icons/ArrowBackIos';

// styles
import useStyles from './SignIn.styles';

// components
import { TextField, Button } from '@src/components';

// api
import { authenticateUser, getVetById } from '@src/api';
import { AuthenticateUserType } from '@src/api/type';

// constants
import {
  TOAST_MESSAGE_SIGNIN_SUCCESS,
  ROLE_TO_ROUTE_MAPPING,
  TOAST_MESSAGE_SIGNIN_FAILURE
} from '@src/constants';

// context
import { ToastContext, HeaderContext } from '@src/context';

// hooks
import { useNavigate } from '@src/hooks';

import { localStorageUtil } from '@src/utils';

interface UserSignIn {
  vetUserId: string;
}

interface LocalStorageSetInput {
  userName: string;
  jwtToken: string;
  role: string;
}

const checkIfUserCanSignIn = async ({ vetUserId }: UserSignIn) => {
  const vet = await getVetById({ vetUserId });

  return vet?.profileStatus === 'APPROVED' ? true : false;
};

const SignIn: React.FC = () => {
  // styles
  const classes = useStyles();

  // context
  const { setToast } = useContext(ToastContext);

  const { setHeader } = useContext(HeaderContext);

  // useEffect
  useEffect(() => {
    setHeader({
      shouldShowHeader: false
    });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // state
  const [userName, setUserName] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const setUserInLocalStorage = ({
    userName,
    jwtToken,
    role
  }: LocalStorageSetInput): void => {
    localStorageUtil.setItem('user', {
      userName,
      jwtToken,
      role
    });
  };

  // submit click
  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    const response: AuthenticateUserType = await authenticateUser({
      username: userName,
      password
    });
    const uName = response.userName;
    const jwtToken = response.jwtToken;
    const role = response.role;

    if (['ROLE_VET', 'VET'].includes(role)) {
      setUserInLocalStorage({
        userName: uName,
        jwtToken,
        role
      });
      const vetIsAllowed = await checkIfUserCanSignIn({ vetUserId: userName });

      if (!vetIsAllowed) {
        setUserInLocalStorage({
          userName: uName,
          jwtToken,
          role
        });
        setToast({
          type: 'error',
          message:
            'Your request is in a pending state, you will receive an email as soon as the admin approves or rejects it.'
        });
        return;
      }
    } else {
      setUserInLocalStorage({
        userName: uName,
        jwtToken,
        role
      });
    }

    const hasError = response.error;
    if (hasError) {
      // display error toast
      setToast({ type: 'error', message: TOAST_MESSAGE_SIGNIN_FAILURE });
    } else {
      setToast({
        type: 'success',
        message: TOAST_MESSAGE_SIGNIN_SUCCESS
      });

      // based on role redirect to respective home page
      navigate(ROLE_TO_ROUTE_MAPPING[response.role], { replace: true });
    }
  };

  const onBackClick = () => {
    navigate('/');
  };

  return (
    <div>
      <div onClick={onBackClick}>
        <IconButton>
          <ArrowBackIosIcon className="back-button-icon" />
        </IconButton>
        <Button color="inherit">Paw pals</Button>
      </div>
      <Container maxWidth="xs" className={classes.root}>
        <Typography variant="h4" align="center" gutterBottom>
          Log In
        </Typography>
        <form onSubmit={handleSubmit}>
          <TextField
            label="User Name"
            type="text"
            value={userName}
            onChange={(event) => setUserName(event.target.value)}
            fullWidth={true}
          />
          <TextField
            label="Password"
            type="password"
            value={password}
            onChange={(event) => setPassword(event.target.value)}
            fullWidth={true}
          />
          <Button
            type="submit"
            variant="contained"
            color="primary"
            disabled={!userName || !password}
            fullWidth={true}
            className={classes.submitButton}
          >
            Sign In
          </Button>
        </form>
        <Typography variant="body2" align="center">
          {"Don't have an account? "}
          <Link component={RouterLink} to="/signup">
            Sign Up
          </Link>
        </Typography>
      </Container>
    </div>
  );
};

export default SignIn;
