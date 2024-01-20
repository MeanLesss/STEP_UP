import React from 'react'
import { Dialog, DialogTitle, DialogContent, Typography } from '@mui/material';
import CloseIcon from '@mui/icons-material/Close';
import IconButton from '@mui/material/IconButton';
import { makeStyles } from '@mui/styles';



const useStyles = makeStyles(theme => ({
    dialogWrapper: {
        position: 'absolute',
    },
    dialogTitle: {
        paddingRight: '0px'
    },
    dialogContent:{
        padding:'0px !important',
        border:'0px !important',

      
    }

    
}));

export default function Popup(props) {
    const { title, children, openPopup, setOpenPopup ,closePopup} = props;
    const classes = useStyles();

    const handleClose = (event, reason) => {
        if (reason === 'backdropClick') {
            setOpenPopup(false);
        }
    };

    return (
        <Dialog 
        open={openPopup} 
        onClose={closePopup} 
        maxWidth="md" 
        classes={{ paper: classes.dialogWrapper }}
        PaperProps={{ style: { borderRadius: '20px' } }} // Add this line
    >
            <DialogContent dividers classes={{ root: classes.dialogContent }}>
                {children}
            </DialogContent>
        </Dialog>
    )
}