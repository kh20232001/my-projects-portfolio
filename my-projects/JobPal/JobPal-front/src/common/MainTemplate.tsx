import CloseIcon from '@mui/icons-material/Close';
import HomeIcon from '@mui/icons-material/Home';
import LogoutIcon from '@mui/icons-material/Logout';
import MenuIcon from '@mui/icons-material/Menu';
import PersonIcon from '@mui/icons-material/Person';
import ReceiptLongIcon from '@mui/icons-material/ReceiptLong';
import TaskIcon from '@mui/icons-material/Task';
import { Typography } from '@mui/material';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Container from '@mui/material/Container';
import CssBaseline from '@mui/material/CssBaseline';
import Drawer from '@mui/material/Drawer';
import IconButton from '@mui/material/IconButton';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import Toolbar from '@mui/material/Toolbar';
import { styled, useTheme } from '@mui/material/styles';
import useMediaQuery from '@mui/material/useMediaQuery';
import { FC, useState } from 'react';
import { Link, Outlet } from 'react-router-dom';



import { useLoginUser } from '../_wheel/security/LoginUserProvider';
import { Authority } from './types/AuthorityTypes';


const drawerWidth = 220; // ドロワーの幅を定義

// メインコンテンツのスタイルを定義
const Main = styled('main', { shouldForwardProp: (prop) => prop !== 'open' && prop !== 'isSmallScreen' })<{
  open?: boolean;
  isSmallScreen?: boolean;
}>(({ theme, open, isSmallScreen }) => ({
  flexGrow: 1,
  padding: theme.spacing(3),
  transition: theme.transitions.create('margin', {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.leavingScreen,
  }),
  marginLeft: isSmallScreen ? 0 : `-${drawerWidth}px`,
  ...(open &&
    !isSmallScreen && {
      transition: theme.transitions.create('margin', {
        easing: theme.transitions.easing.easeOut,
        duration: theme.transitions.duration.enteringScreen,
      }),
      marginLeft: 0,
    }),
  ...(open &&
    !isSmallScreen && {
      width: `calc(100% - ${drawerWidth}px)`,
    }),
}));

// AppBarのスタイルを定義
const AppBarStyled = styled(AppBar, {
  shouldForwardProp: (prop) => prop !== 'open',
})<{
  open?: boolean;
}>(({ theme, open }) => ({
  transition: theme.transitions.create(['margin', 'width'], {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.leavingScreen,
  }),
  ...(open && {
    width: `calc(100% - ${drawerWidth}px)`,
    marginLeft: `${drawerWidth}px`,
    transition: theme.transitions.create(['margin', 'width'], {
      easing: theme.transitions.easing.easeOut,
      duration: theme.transitions.duration.enteringScreen,
    }),
  }),
}));

// MainTemplateコンポーネントを定義
export const MainTemplate: FC = () => {
  const { logout, getAuthority } = useLoginUser();

  const theme = useTheme();
  const [drawerOpen, setDrawerOpen] = useState(false); // ドロワーの開閉状態を管理

  const handleDrawerOpen = () => {
    setDrawerOpen(true); // ドロワーを開く
  };

  const handleDrawerClose = () => {
    setDrawerOpen(false); // ドロワーを閉じる
  };

  const isSmallScreen = useMediaQuery(theme.breakpoints.down('sm'));

  // 権限がユーザーの場合のメニュー項目
  const AuthMenuItems = [
    { text: 'ダッシュボード', path: '/home', icon: <HomeIcon /> },
    { text: '新規活動申請', path: '/new', icon: <TaskIcon /> },
    { text: '証明書', path: '/certificate', icon: <ReceiptLongIcon /> },
    { text: 'Logout', path: '/logout', icon: <LogoutIcon />, action: () => logout(false) },
  ];

  // 権限が管理者の場合のメニュー項目
  const AdminMenuItems = [
    { text: 'ダッシュボード', path: '/home', icon: <HomeIcon /> },
    { text: 'ユーザ管理', path: '/user', icon: <PersonIcon /> },
    { text: '証明書', path: '/certificate', icon: <ReceiptLongIcon /> },
    { text: 'Logout', path: '/logout', icon: <LogoutIcon />, action: () => logout(false) },
  ];

  // 権限が事務の場合のメニュー項目
  const ClerkMenuItems = [
    { text: '証明書', path: '/certificate', icon: <ReceiptLongIcon /> },
    { text: 'Logout', path: '/logout', icon: <LogoutIcon />, action: () => logout(false) },
  ];

  return (
    <Box sx={{ display: 'flex' }}>
      <CssBaseline />
      <AppBarStyled position="fixed" open={drawerOpen && !isSmallScreen}>
        <Toolbar style={{ display: 'flex', justifyContent: 'space-between' }}>
          <Box style={{ display: 'flex', alignItems: 'center' }}>
            <IconButton
              color="inherit"
              aria-label="open drawer"
              edge="start"
              onClick={handleDrawerOpen}
              sx={{ mr: 2, ...(drawerOpen && !isSmallScreen && { display: 'none' }) }}
            >
              <MenuIcon />
            </IconButton>
            <Box>
                <Link to={getAuthority() === Authority.clerk ? "/certificate" : "/home"} style={{ color: 'inherit', textDecoration: 'none' }}>
                <Typography variant="h6">JobPal</Typography>
                </Link>
            </Box>
          </Box>
        </Toolbar>
      </AppBarStyled>
      <Drawer
        sx={{
          width: drawerWidth,
          flexShrink: 0,
          '& .MuiDrawer-paper': {
            width: drawerWidth,
            boxSizing: 'border-box',
          },
        }}
        variant={isSmallScreen ? 'temporary' : 'persistent'}
        anchor="left"
        open={drawerOpen}
        onClose={handleDrawerClose}
      >
        <Toolbar>
          <IconButton onClick={handleDrawerClose}>
            {theme.direction === 'ltr' ? <CloseIcon /> : <MenuIcon />}
          </IconButton>
        </Toolbar>
        <List>
          {getAuthority() === Authority.user &&
            AuthMenuItems.map((item, index) => (
              <div key={index} onClick={handleDrawerClose}>
                <ListItem
                  button
                  component={item.path !== '/logout' ? Link : 'div'}
                  to={item.path !== '/logout' ? item.path : undefined}
                  onClick={item.action}
                >
                  <ListItemIcon>{item.icon}</ListItemIcon>
                  <ListItemText primary={item.text} />
                </ListItem>
              </div>
            ))}
          {(getAuthority() === Authority.teacher || getAuthority() === Authority.admin) &&
            AdminMenuItems.map((item, index) => (
              <div key={index} onClick={handleDrawerClose}>
                <ListItem
                  button
                  component={item.path !== '/logout' ? Link : 'div'}
                  to={item.path !== '/logout' ? item.path : undefined}
                  onClick={item.action}
                >
                  <ListItemIcon>{item.icon}</ListItemIcon>
                  <ListItemText primary={item.text} />
                </ListItem>
              </div>
            ))}
          {getAuthority() === Authority.clerk &&
            ClerkMenuItems.map((item, index) => (
              <div key={index} onClick={handleDrawerClose}>
                <ListItem
                  button
                  component={item.path !== '/logout' ? Link : 'div'}
                  to={item.path !== '/logout' ? item.path : undefined}
                  onClick={item.action}
                >
                  <ListItemIcon>{item.icon}</ListItemIcon>
                  <ListItemText primary={item.text} />
                </ListItem>
              </div>
            ))}
        </List>
      </Drawer>
      <Main open={drawerOpen} isSmallScreen={isSmallScreen} sx={{ padding: '8px' }}>
        <Container sx={{ marginTop: 10, marginBottom: 2 }}>
          <Outlet />
        </Container>
      </Main>
    </Box>
  );
};
