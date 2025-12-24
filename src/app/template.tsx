"use client";

import { animatePageIn } from "@/utils/animations";
import React, { useEffect } from "react";

export default function Template({ children }: { children: React.ReactNode }) {
  useEffect(() => {
    animatePageIn();
  }, []);
  return (
    <div>
      <div
        id="banner-1"
        className="fixed left-0 top-0 z-[999999] min-h-screen w-1/4 bg-neutral-950"
      />
      <div
        id="banner-2"
        className="fixed left-1/4 top-0 z-[999999] min-h-screen w-1/4 bg-neutral-950"
      />
      <div
        id="banner-3"
        className="fixed left-2/4 top-0 z-[999999] min-h-screen w-1/4 bg-neutral-950"
      />
      <div
        id="banner-4"
        className="fixed left-3/4 top-0 z-[999999] min-h-screen w-1/4 bg-neutral-950"
      />
      {children}
    </div>
  );
}
