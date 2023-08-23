import { createSlice } from '@reduxjs/toolkit';

interface MessageState {
    content: string;
    receiver: string;
    sender: string;
    allMessages: object[];
}

const initialState: MessageState = {
    content: '',
    receiver: '',
    sender: '',
    allMessages: [],
};

export const messageSlice = createSlice({
    name: 'message',
    initialState,
    reducers: {
        setContent: (state, action) => {
            state.content = action.payload;
        },
        setSender: (state, action) => {
            state.sender = action.payload;
        },
        setReceiver: (state, action) => {
            state.receiver = action.payload;
        },
        addMessage: (state, action) => {
            state.allMessages.push(JSON.parse(action.payload));
        }
    },
});


export const { setContent, setReceiver, addMessage, setSender } = messageSlice.actions;
export default messageSlice.reducer;
