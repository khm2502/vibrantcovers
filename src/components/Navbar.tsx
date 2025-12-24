import Link from "next/link";
import MaxWidthWrapper from "./MaxWidthWrapper";
import { buttonVariants } from "./ui/button";
import { ArrowRight } from "lucide-react";
import { getKindeServerSession } from "@kinde-oss/kinde-auth-nextjs/server";

const Navbar = async () => {
  const { getUser } = getKindeServerSession();
  const user = await getUser();

  const isAdmin = user?.email === process.env.ADMIN_EMAIL;

  return (
    <nav className="sticky inset-x-0 z-[100] mx-auto w-full bg-dark p-3 backdrop-blur-lg transition-all">
      <MaxWidthWrapper>
        <div className="flex h-14 items-center justify-between">
          <Link
            href="/"
            className="z-40 flex font-semibold  text-white sm:text-2xl"
          >
            Vibrant<span className="text-red-600">Covers</span>
          </Link>

          <div className="flex h-full items-center space-x-4">
            {user ? (
              <>
                <Link
                  href="/api/auth/logout"
                  className={buttonVariants({
                    size: "sm",
                    variant: "link",
                  })}
                >
                  <p className="text-center text-white sm:text-base">
                    Sign out
                  </p>
                </Link>
                {isAdmin ? (
                  <Link
                    href="/dashboard"
                    className={buttonVariants({
                      size: "sm",
                      variant: "link",
                    })}
                  >
                    <p className="text-center text-white sm:text-base">
                      Dashboard 🖥️
                    </p>
                  </Link>
                ) : null}
                <Link
                  href="/configure/upload"
                  className={buttonVariants({
                    size: "sm",
                    className:
                      "mx-auto hidden sm:flex items-center gap-1 text-white relative rounded-md px-10 py-3 text-lg font-semibold transition-all hover:-translate-x-1 hover:-translate-y-1 hover:shadow-[4px_4px_0_rgb(255,255,255)] hover:after:absolute hover:after:-bottom-2 hover:after:-right-2 hover:after:left-0 hover:after:top-0 hover:after:z-10",
                  })}
                >
                  <p className="text-center text-white sm:text-base">
                    Create case
                  </p>
                  <ArrowRight className="ml-1.5 size-5" />
                </Link>
              </>
            ) : (
              <>
                <Link
                  href="/api/auth/register"
                  className={buttonVariants({
                    size: "sm",
                    variant: "link",
                  })}
                >
                  <p className="text-center text-white sm:text-base">Sign up</p>
                </Link>

                <Link
                  href="/api/auth/login"
                  className={buttonVariants({
                    size: "sm",
                    variant: "link",
                  })}
                >
                  <p className="text-center text-white sm:text-base">Login</p>
                </Link>

                <div className="hidden h-8 w-px bg-white sm:block" />

                <Link
                  href="/configure/upload"
                  className={buttonVariants({
                    size: "sm",
                    className:
                      "mx-auto hidden sm:flex items-center gap-1 text-white relative rounded-md px-10 py-3 text-lg font-semibold transition-all hover:-translate-x-1 hover:-translate-y-1 hover:shadow-[4px_4px_0_rgb(255,255,255)] hover:after:absolute hover:after:-bottom-2 hover:after:-right-2 hover:after:left-0 hover:after:top-0 hover:after:z-10",
                  })}
                >
                  <p className="text-center sm:text-base">Create case</p>
                  <ArrowRight className="ml-1.5 size-5" />
                </Link>
              </>
            )}
          </div>
        </div>
      </MaxWidthWrapper>
    </nav>
  );
};

export default Navbar;
