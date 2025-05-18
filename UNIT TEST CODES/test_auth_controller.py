import pytest
import requests

BASE_URL = "http://localhost:8080/api/securities/auth"

def test_login():
    payload = {"username": "admin", "password": "admin"}
    response = requests.post(f"{BASE_URL}/login", json=payload)
    assert response.status_code == 200, f"Expected 200, got {response.status_code}"
    assert response.json()["data"]["accessToken"] is not None

def test_register():
    payload = {
        "username": "testuser",
        "password": "testpassword",
        "email": "testuser@example.com",
        "fullName": "Test User"
    }
    response = requests.post(f"{BASE_URL}/register", json=payload)
    assert response.status_code == 201, f"Expected 201, got {response.status_code}"
    assert response.json()["data"]["username"] == "testuser"

def test_forgot_password():
    response = requests.post(f"{BASE_URL}/forgot-password?email=testuser@example.com")
    assert response.status_code == 200, f"Expected 200, got {response.status_code}"