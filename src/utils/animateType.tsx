"use client";

import { TypeAnimation } from "react-type-animation";

const AnimateType = () => {
  return (
    <TypeAnimation
      sequence={[`Phone`, 2000, `Image`, 2000, `Story`, 2000]}
      wrapper="span"
      className="text-gray-500"
      repeat={Infinity}
    />
  );
};

export default AnimateType;
