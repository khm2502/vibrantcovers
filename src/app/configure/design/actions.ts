"use server";

import apiClient from "@/lib/api-client";

export type SaveConfigArgs = {
  color: string;
  finish: string;
  material: string;
  model: string;
  configId: string;
};

export async function saveConfig({
  color,
  finish,
  material,
  model,
  configId,
}: SaveConfigArgs) {
  await apiClient.saveConfiguration(configId, {
    color: color.toUpperCase(),
    finish: finish.toUpperCase(),
    material: material.toUpperCase(),
    model: model.toUpperCase(),
  });
}
