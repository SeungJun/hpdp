import React, { useState, useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { messageSliceActions } from "../../store/message-slice";
import * as Interfaces from "../../interface/apiDataInterface";
import { getMessage } from "../../api/messages";
import MessagePart from "./MessagePart";
import { Icon } from "@iconify/react";
import { Grid } from "@mui/material";
import style from "../../styles/css/SendingMessage.module.css";
import NullModal from "../common/NullModal";

const SendingMessage = () => {
  const accessToken = useSelector((state: any) => state.user.auth.accessToken);
  const [messageData, setMessageData] = useState<
    Interfaces.MessagesInterface[]
  >([]);

  useEffect(() => {
    getMessage(
      accessToken,
      1,
      (res) => {
        setMessageData(res.data.data);
      },
      (err) => {
        console.log(err);
      }
    );
  }, []);

  return (
    <div className={style.message}>
      <div className={style.down_content}>
        {messageData.length > 0 ? (
          messageData.map((message, index) => (
            <div key={message.messageId}>
              <MessagePart key={message.messageId} message={message} flag={1} />
            </div>
          ))
        ) : (
          <NullModal text="보낸 쪽지가 없습니다." />
        )}
      </div>
    </div>
  );
};

export default SendingMessage;
