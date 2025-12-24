"use client";

import React, { useRef, useEffect } from "react";
import { motion, useScroll, useTransform, motionValue } from "framer-motion";
import { customers } from "@/lib/data";
import Lenis from "lenis";
import { Check, Star } from "lucide-react";

const Card = ({
  i,
  title,
  description,
  src,
  name,
  color,
  range,
  targetScale,
  progress,
}: {
  i: number;
  title: string;
  description: string;
  src: string;
  name: string;
  color: string;
  range: number[];
  targetScale: number;
  progress: number;
}) => {
  const container = useRef(null);
  const { scrollYProgress } = useScroll({
    target: container,
    offset: ["start end", "start start"],
  });
  const imageScale = useTransform(scrollYProgress, [0, 1], [2, 1]); // image zoom out animation
  const scale = useTransform(motionValue(progress), range, [1, targetScale]); // stacking effect

  return (
    <div
      ref={container}
      className="sticky top-0 flex h-screen items-center justify-center"
    >
      <motion.div
        className="relative flex h-[700px] w-[1000px] flex-col rounded-3xl p-6 md:h-[500px] md:p-12"
        style={{
          scale,
          backgroundColor: color,
          top: `calc(-5vh + ${i * 25}px)`,
        }}
      >
        <div className="flex h-full flex-col items-center justify-center space-y-3 md:flex-row md:space-y-0">
          <div className="flex size-full flex-col items-center justify-center gap-4 md:w-2/5">
            <div className="relative overflow-hidden rounded-full">
              <motion.div
                style={{ scale: imageScale }}
                className="relative size-full"
              >
                <img
                  src={`/images/${src}`}
                  alt="image"
                  className="size-52 rounded-full object-cover"
                />
              </motion.div>
            </div>
            <p className="text-center text-3xl font-bold !leading-tight tracking-tight md:text-4xl">
              {name}
            </p>
            <div className="ms:text-3xl flex flex-row text-center text-2xl font-semibold">
              <span className="mr-1 flex items-center justify-center">
                <Check />
              </span>
              {title}
            </div>
          </div>
          <div className="flex w-full flex-col space-y-0 text-balance text-center text-base md:w-3/5 md:space-y-2 md:text-left md:text-xl">
            <div>"{description}"</div>
            <div className="mb-2 flex gap-0.5">
              <Star className="size-5 fill-dark text-dark" />
              <Star className="size-5 fill-dark text-dark" />
              <Star className="size-5 fill-dark text-dark" />
              <Star className="size-5 fill-dark text-dark" />
              <Star className="size-5 fill-dark text-dark" />
            </div>
          </div>
        </div>
      </motion.div>
    </div>
  );
};

export default function CardParallax() {
  const container = useRef(null);
  const { scrollYProgress } = useScroll({
    target: container,
    offset: ["start start", "end end"],
  });

  useEffect(() => {
    const lenis = new Lenis();

    function raf(time: number) {
      lenis.raf(time);
      requestAnimationFrame(raf);
    }

    requestAnimationFrame(raf);
  }, []);

  return (
    <main className="relative mt-[10vh]">
      {customers.map((customer, index) => {
        const targetScale = 1 - (customers.length - index) * 0.05;
        return (
          <Card
            key={index}
            i={index}
            {...customer}
            progress={scrollYProgress.get()}
            range={[index * 0.25, 1]}
            targetScale={targetScale}
          />
        );
      })}
    </main>
  );
}
