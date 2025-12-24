import { createUploadthing, type FileRouter } from "uploadthing/next";
import { z } from "zod";
import sharp from "sharp";

const f = createUploadthing();

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

export const ourFileRouter = {
  imageUploader: f({ image: { maxFileSize: "4MB" } })
    .input(z.object({ configId: z.string().optional() }))
    .middleware(async ({ input }) => {
      return { input };
    })
    .onUploadComplete(async ({ metadata, file }) => {
      const { configId } = metadata.input;

      // Download image to get dimensions
      const res = await fetch(file.url);
      const buffer = await res.arrayBuffer();

      const imgMetadata = await sharp(buffer).metadata();
      const { width, height } = imgMetadata;

      // Call Spring Boot UploadThing callback endpoint
      try {
        const response = await fetch(`${API_BASE_URL}/uploadthing/callback`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify({
            file: {
              url: file.url,
              name: file.name,
              size: file.size,
              key: file.key,
            },
            metadata: {
              input: {
                configId: configId || null,
              },
            },
          }),
        });

        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(`Spring Boot callback failed: ${errorText}`);
        }

        const data = await response.json();
        return { configId: data.configId || data.id || configId };
      } catch (error) {
        console.error('Error calling Spring Boot UploadThing callback:', error);
        throw error;
      }
    }),
} satisfies FileRouter;

export type OurFileRouter = typeof ourFileRouter;
