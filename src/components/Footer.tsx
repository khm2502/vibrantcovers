import Link from "next/link";
import MaxWidthWrapper from "./MaxWidthWrapper";
import { buttonVariants } from "./ui/button";

const Footer = () => {
  return (
    <footer className="relative h-20 bg-dark">
      <MaxWidthWrapper>
        <div className="border-t border-gray-200" />

        <div className="flex h-full flex-col items-center justify-center md:flex-row md:justify-between">
          <div className="pb-2 text-center md:pb-0 md:text-left">
            <p className="text-center text-white sm:text-base">
              &copy; {new Date().getFullYear()} All rights reserved
            </p>
          </div>

          <div className="flex items-center justify-center">
            <div className="flex items-center justify-between md:space-x-8">
              <Link
                href="#"
                className={buttonVariants({
                  size: "sm",
                  variant: "link",
                })}
              >
                <p className="text-center text-white sm:text-base">Terms</p>
              </Link>
              <Link
                href="#"
                className={buttonVariants({
                  size: "sm",
                  variant: "link",
                })}
              >
                <p className="text-center text-white sm:text-base">
                  Privacy Policy
                </p>
              </Link>
              <Link
                href="#"
                className={buttonVariants({
                  size: "sm",
                  variant: "link",
                })}
              >
                <p className="text-center text-white sm:text-base">
                  Cookie Policy
                </p>
              </Link>
            </div>
          </div>
        </div>
      </MaxWidthWrapper>
    </footer>
  );
};

export default Footer;
