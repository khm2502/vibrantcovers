import apiClient from "@/lib/api-client";
import { notFound } from "next/navigation";
import DesignPreview from "./DesignPreview";

interface PageProps {
  searchParams: {
    [key: string]: string | string[] | undefined;
  };
}

const Page = async ({ searchParams }: PageProps) => {
  const { id } = searchParams;

  if (!id || typeof id !== "string") {
    return notFound();
  }

  // Fetch configuration from Spring Boot backend
  let configuration: any;
  try {
    configuration = await apiClient.getConfiguration(id);
  } catch {
    return notFound();
  }

  if (!configuration) {
    return notFound();
  }

  return <DesignPreview configuration={configuration} />;
};

export default Page;
