/**
 * API Client for Spring Boot Backend
 * Base URL: http://localhost:8080/api
 */

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

interface ApiError {
  message: string;
  status?: number;
}

class ApiClient {
  private baseUrl: string;

  constructor(baseUrl: string = API_BASE_URL) {
    this.baseUrl = baseUrl;
  }

  private async request<T>(
    endpoint: string,
    options: RequestInit = {}
  ): Promise<T> {
    const url = `${this.baseUrl}${endpoint}`;
    
    // Get user info from Kinde (if available)
    const headers: HeadersInit = {
      'Content-Type': 'application/json',
      ...options.headers,
    };

    try {
      const response = await fetch(url, {
        ...options,
        headers,
      });

      if (!response.ok) {
        const errorText = await response.text();
        let errorMessage = `Request failed: ${response.statusText}`;
        
        try {
          const errorJson = JSON.parse(errorText);
          errorMessage = errorJson.message || errorJson.error || errorMessage;
        } catch {
          errorMessage = errorText || errorMessage;
        }

        const error: ApiError = {
          message: errorMessage,
          status: response.status,
        };
        throw error;
      }

      // Handle empty responses
      const contentType = response.headers.get('content-type');
      if (contentType && contentType.includes('application/json')) {
        return await response.json();
      }
      
      return {} as T;
    } catch (error) {
      if (error instanceof Error) {
        throw { message: error.message } as ApiError;
      }
      throw error;
    }
  }

  // Configuration endpoints
  async getConfiguration(id: string): Promise<any> {
    return this.request(`/configurations/${id}`);
  }

  async saveConfiguration(configId: string, data: {
    color: string;
    finish: string;
    material: string;
    model: string;
  }): Promise<void> {
    return this.request(`/configurations/${configId}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  }

  // Upload endpoint
  async uploadImage(file: File, configId?: string): Promise<{ configId: string }> {
    const formData = new FormData();
    formData.append('file', file);
    if (configId) {
      formData.append('configId', configId);
    }

    const url = `${this.baseUrl}/upload`;
    
    // Get user info from Kinde (if available)
    const headers: HeadersInit = {};

    const response = await fetch(url, {
      method: 'POST',
      headers,
      body: formData,
    });

    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(errorText || 'Upload failed');
    }

    return await response.json();
  }

  // Checkout endpoint
  async createCheckoutSession(configId: string, userId: string, userEmail: string): Promise<{ url: string }> {
    return this.request('/checkout', {
      method: 'POST',
      headers: {
        'X-User-Id': userId,
        'X-User-Email': userEmail,
      },
      body: JSON.stringify({ configId }),
    });
  }

  // Order endpoints
  async getOrderStatus(orderId: string, userId: string, userEmail: string): Promise<any> {
    return this.request(`/orders/${orderId}/status`, {
      headers: {
        'X-User-Id': userId,
        'X-User-Email': userEmail,
      },
    });
  }

  async updateOrderStatus(orderId: string, status: string, userId: string, userEmail: string): Promise<void> {
    return this.request(`/orders/${orderId}/status`, {
      method: 'PUT',
      headers: {
        'X-User-Id': userId,
        'X-User-Email': userEmail,
      },
      body: JSON.stringify({ status }),
    });
  }

  // Auth endpoint
  async createUser(userId: string, email: string): Promise<{ success: boolean }> {
    return this.request('/auth/callback', {
      method: 'POST',
      headers: {
        'X-User-Id': userId,
        'X-User-Email': email,
      },
    });
  }

  // Dashboard endpoint
  async getDashboardData(userId: string, userEmail: string): Promise<any> {
    return this.request('/orders/dashboard', {
      headers: {
        'X-User-Id': userId,
        'X-User-Email': userEmail,
      },
    });
  }
}

export const apiClient = new ApiClient();
export default apiClient;




