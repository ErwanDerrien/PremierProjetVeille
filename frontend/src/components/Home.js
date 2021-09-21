import { Typography } from "@mui/material";
import React from "react";

const Home = () => {
  const [open, setOpen] = React.useState(true);

  return (
    <>
      <Typography
        variant="h1"
        component="h1"
        sx={{
          clear: "right",
        }}
      >
        Home
      </Typography>
    </>
  );
};

export default Home;
