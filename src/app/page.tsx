import { Icons } from "@/components/Icons";
import MaxWidthWrapper from "@/components/MaxWidthWrapper";
import Phone from "@/components/Phone";
import { Reviews } from "@/components/Reviews";
import { buttonVariants } from "@/components/ui/button";
import { ArrowRight, Check, TargetIcon } from "lucide-react";
import Link from "next/link";
import AnimateType from "@/utils/animateType";
import { Suspense } from "react";
import CardParallax from "@/components/CardParallax";

export default function Home() {
  return (
    <div className="grainy-light bg-slate-50">
      <section className="md:pb-24">
        <MaxWidthWrapper className="pb-24 pt-10 md:py-24 xl:py-32">
          <div className="flex-col items-center justify-center space-y-6 text-center text-dark">
            <p className="relative text-5xl font-bold !leading-tight tracking-tight md:text-6xl lg:text-7xl">
              Make <span className="bg-red-600 px-2 text-white">Memories</span>{" "}
              Last <br /> Print Them on Your Case
            </p>
            <p className="text-5xl">
              <span>Your</span>{" "}
              <Suspense fallback={<div>Loading...</div>}>
                <AnimateType />
              </Suspense>
            </p>
            <p className="w-full text-xl">
              Turn your photos, artwork, or wildest ideas into phone cases that
              showcase your personality. Capture your favorite memories with
              your own, <span className="font-semibold">one-of-one</span> phone
              case. VibrantCovers allows you to protect your memories, not just
              your phone case.
            </p>
          </div>
        </MaxWidthWrapper>

        <MaxWidthWrapper className="bg-dark pb-24 pt-10 text-white md:rounded-3xl lg:grid lg:grid-cols-3 lg:gap-x-0">
          <div className="col-span-2 px-6 lg:px-0 lg:pt-4">
            <div className="relative flex flex-col items-center justify-center text-center lg:items-start lg:text-left">
              <h1 className="relative mt-16 w-fit text-5xl font-bold !leading-tight tracking-tight md:text-6xl">
                We provide top quality and benefits
              </h1>
              <p className="mt-8 max-w-prose text-balance text-center text-lg md:text-wrap lg:pr-10 lg:text-left">
                Quality:
              </p>

              <ul className="mt-2 flex flex-col items-center space-y-2 text-left font-medium sm:items-start">
                <div className="space-y-2">
                  <li className="flex items-center gap-1.5 text-left">
                    <Check className="size-5 shrink-0 text-red-600" />
                    High-quality, durable material
                  </li>
                  <li className="flex items-center gap-1.5 text-left">
                    <Check className="size-5 shrink-0 text-red-600" />
                    High-Resolution Printing
                  </li>
                  <li className="flex items-center gap-1.5 text-left">
                    <Check className="size-5 shrink-0 text-red-600" />5 year
                    print guarantee
                  </li>
                </div>
              </ul>

              <p className="mt-8 max-w-prose text-balance text-center text-lg md:text-wrap lg:pr-10 lg:text-left">
                Features:
              </p>

              <ul className="mt-2 flex flex-col items-center space-y-2 text-left font-medium sm:items-start">
                <div className="space-y-2">
                  <li className="flex items-center gap-1.5 text-left">
                    <Check className="size-5 shrink-0 text-red-600" />
                    Easy to Design
                  </li>
                  <li className="flex items-center gap-1.5 text-left">
                    <Check className="size-5 shrink-0 text-red-600" />
                    Customization Options
                  </li>
                  <li className="flex items-center gap-1.5 text-left">
                    <Check className="size-5 shrink-0 text-red-600" />
                    Modern iPhone models supported
                  </li>
                </div>
              </ul>
            </div>
          </div>

          <div className="col-span-full mt-32 flex h-fit w-full justify-center px-8 sm:px-16 md:px-0 lg:col-span-1 lg:mx-0 lg:mt-20">
            <div className="relative md:max-w-xl">
              <img
                src="/your-image.svg"
                className="absolute -top-32 left-48 hidden w-40 select-none sm:block lg:hidden lg:w-52 xl:block"
                alt="img"
              />
              <img
                src="/line.png"
                className="absolute -bottom-6 -left-6 w-20 select-none"
                alt="line"
              />
              <Phone
                dark={true}
                className="w-64"
                imgSrc="/testimonials/4.jpeg"
              />
            </div>
          </div>
        </MaxWidthWrapper>
      </section>

      {/* value proposition section */}
      <section className="bg-dark py-24">
        <MaxWidthWrapper className="flex flex-col items-center gap-0">
          <div className="flex w-full flex-col justify-center gap-8 sm:gap-6">
            <h2 className="mt-2 text-balance text-center text-5xl font-bold !leading-tight tracking-tight text-white md:text-left md:text-6xl">
              Don't Take{" "}
              <span className="relative px-2">
                Our Word
                <Icons.underline className="pointer-events-none absolute inset-x-0 -bottom-6 hidden text-red-500 sm:block" />
              </span>{" "}
              For It!
            </h2>
            <h3 className="text-center text-2xl font-semibold !leading-tight tracking-tight text-white md:text-right md:text-4xl">
              <TargetIcon className="hidden size-12 sm:inline" /> Here are some
              of the <br className="hidden sm:inline" /> insights from our
              clients
            </h3>
          </div>
        </MaxWidthWrapper>

        <CardParallax />
        <div className="pt-4">
          <Reviews />
        </div>
      </section>

      <section className="grainy-dark bg-slate-100 py-24">
        <MaxWidthWrapper>
          <div className="mb-12 flex items-center justify-center space-y-6 text-center text-dark">
            <p className="relative text-5xl font-bold !leading-tight tracking-tight md:text-6xl">
              Design Your Case, Own Your Style
              <br />
              <span className="bg-red-600 px-2 text-white">Start Now!</span>
            </p>
          </div>

          <div className="mx-auto max-w-6xl px-6 lg:px-8">
            <div className="relative flex grid-cols-2 flex-col items-center gap-40 md:grid">
              <img
                src="/arrow.png"
                className="absolute left-1/2 top-[25rem] z-10 -translate-x-1/2 -translate-y-1/2 rotate-90 md:top-1/2 md:rotate-0"
              />

              <div className="relative h-80 w-full max-w-sm rounded-xl bg-gray-900/5 ring-inset ring-gray-900/10 md:h-full md:justify-self-end lg:rounded-2xl">
                <img
                  src="/testimonials/last.jpeg"
                  className="size-full rounded-md bg-white object-cover shadow-2xl ring-1 ring-gray-900/10"
                />
              </div>

              <Phone className="w-60" imgSrc="/testimonials/last.jpeg" />
            </div>
          </div>

          <ul className="mx-auto mt-12 w-fit max-w-prose space-y-2 text-dark sm:text-lg">
            <div className="flex justify-center">
              <Link
                className={buttonVariants({
                  size: "lg",
                  className:
                    "mx-auto mt-8 hidden sm:flex items-center gap-1 text-white relative rounded-md px-10 py-3 text-lg font-semibold transition-all hover:-translate-x-1 hover:-translate-y-1 hover:shadow-[4px_4px_0_rgb(41,37,36)] hover:after:absolute hover:after:-bottom-2 hover:after:-right-2 hover:after:left-0 hover:after:top-0 hover:after:z-10",
                })}
                href="/configure/upload"
              >
                Create your case now <ArrowRight className="ml-1.5 size-4" />
              </Link>
            </div>
          </ul>
        </MaxWidthWrapper>
      </section>
    </div>
  );
}
