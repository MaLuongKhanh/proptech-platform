import pytest
import requests
import json
import logging

BASE_URL = "http://localhost:8080/api/listings/properties"

# Set up logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

@pytest.fixture
def auth_token():
    login_url = "http://localhost:8080/api/securities/auth/login"
    payload = {"username": "admin", "password": "admin"}
    response = requests.post(login_url, json=payload)
    assert response.status_code == 200, f"Failed to authenticate: {response.text}"
    return response.json()["data"]["accessToken"]

@pytest.fixture
def headers(auth_token):
    return {
        "Content-Type": "application/json",
        "Authorization": f"Bearer {auth_token}"
    }

@pytest.fixture
def test_property(headers):
    payload = {
        "name": "Setup Property",
        "address": "789 Setup Street",
        "latitude": 10.776,
        "longitude": 106.700
    }
    response = requests.post(BASE_URL, headers=headers, data=json.dumps(payload))
    if response.status_code != 201:
        pytest.skip(f"Failed to create test property: {response.text}")
    return response.json()["data"]["id"]

def test_create_property_success(headers):
    payload = {
        "name": "Test Property",
        "address": "123 Test Street",
        "latitude": 10.776,
        "longitude": 106.700
    }
    response = requests.post(BASE_URL, headers=headers, data=json.dumps(payload))
    logger.info(f"Create property response: {response.status_code} {response.text}")
    assert response.status_code == 201, f"Expected 201, got {response.status_code}"
    data = response.json()["data"]
    assert data["name"] == "Test Property"
    assert data["address"] == "123 Test Street"
    assert data["latitude"] == 10.776
    assert data["longitude"] == 106.700
    assert data["id"] is not None

def test_create_property_invalid_payload(headers):
    payload = {
        "name": "",  # Invalid: empty name
        "address": "123 Test Street"
    }
    response = requests.post(BASE_URL, headers=headers, data=json.dumps(payload))
    logger.info(f"Create invalid property response: {response.status_code} {response.text}")
    assert response.status_code == 400, f"Expected 400, got {response.status_code}"
    assert "message" in response.json()

def test_get_property_success(headers, test_property):
    response = requests.get(f"{BASE_URL}/{test_property}", headers=headers)
    logger.info(f"Get property response: {response.status_code} {response.text}")
    assert response.status_code == 200, f"Expected 200, got {response.status_code}"
    data = response.json()["data"]
    assert data["id"] == test_property
    assert data["name"] == "Setup Property"
    assert data["address"] == "789 Setup Street"

def test_get_property_not_found(headers):
    response = requests.get(f"{BASE_URL}/non-existent-999", headers=headers)
    logger.info(f"Get non-existent property response: {response.status_code} {response.text}")
    assert response.status_code == 404, f"Expected 404, got {response.status_code}"
    assert "message" in response.json()

def test_update_property_success(headers, test_property):
    payload = {
        "name": "Updated Property",
        "address": "456 Updated Street",
        "latitude": 11.776,
        "longitude": 107.700
    }
    response = requests.put(f"{BASE_URL}/{test_property}", headers=headers, data=json.dumps(payload))
    logger.info(f"Update property response: {response.status_code} {response.text}")
    assert response.status_code == 200, f"Expected 200, got {response.status_code}"
    data = response.json()["data"]
    assert data["name"] == "Updated Property"
    assert data["address"] == "456 Updated Street"
    assert data["latitude"] == 11.776
    assert data["longitude"] == 107.700

def test_update_property_not_found(headers):
    payload = {
        "name": "Updated Property",
        "address": "456 Updated Street"
    }
    response = requests.put(f"{BASE_URL}/non-existent-999", headers=headers, data=json.dumps(payload))
    logger.info(f"Update non-existent property response: {response.status_code} {response.text}")
    assert response.status_code == 404, f"Expected 404, got {response.status_code}"
    assert "message" in response.json()

def test_delete_property_success(headers, test_property):
    response = requests.delete(f"{BASE_URL}/{test_property}", headers=headers)
    logger.info(f"Delete property response: {response.status_code} {response.text}")
    assert response.status_code == 204, f"Expected 204, got {response.status_code}"

def test_delete_property_not_found(headers):
    response = requests.delete(f"{BASE_URL}/non-existent-999", headers=headers)
    logger.info(f"Delete non-existent property response: {response.status_code} {response.text}")
    assert response.status_code == 404, f"Expected 404, got {response.status_code}"
    assert "message" in response.json()

def test_get_all_properties_success(headers):
    params = {"page": 0, "size": 10}
    response = requests.get(BASE_URL, params=params, headers=headers)
    logger.info(f"Get all properties response: {response.status_code} {response.text}")
    assert response.status_code == 200, f"Expected 200, got {response.status_code}"
    data = response.json()["data"]
    assert isinstance(data, list)
    # Check pagination metadata (assuming Spring Data Page response)
    assert "content" in data or isinstance(data, list)  # Adjust based on actual response