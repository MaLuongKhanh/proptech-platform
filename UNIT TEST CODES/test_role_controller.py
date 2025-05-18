import pytest
import requests
import logging

BASE_URL = "http://localhost:8080/api/securities/roles"

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
def test_role(auth_token):
    headers = {"Authorization": f"Bearer {auth_token}"}
    payload = {
        "name": "TEST_ROLE",
        "description": "Test role"
    }
    response = requests.post(BASE_URL, json=payload, headers=headers)
    if response.status_code != 201:
        pytest.skip(f"Failed to create test role: {response.text}")
    return response.json()["data"]["id"]

def test_create_role(auth_token):
    headers = {"Authorization": f"Bearer {auth_token}"}
    payload = {
        "name": "NEW_TEST_ROLE",
        "description": "New test role"
    }
    response = requests.post(BASE_URL, json=payload, headers=headers)
    logger.info(f"Create role response: {response.status_code} {response.text}")
    assert response.status_code == 201, f"Expected 201, got {response.status_code}"
    assert response.json()["data"]["name"] == "NEW_TEST_ROLE"

def test_get_all_roles(auth_token):
    headers = {"Authorization": f"Bearer {auth_token}"}
    response = requests.get(BASE_URL, headers=headers)
    logger.info(f"Get all roles response: {response.status_code} {response.text}")
    assert response.status_code == 200, f"Expected 200, got {response.status_code}"
    assert isinstance(response.json()["data"], list)

def test_get_role_by_id(auth_token, test_role):
    headers = {"Authorization": f"Bearer {auth_token}"}
    response = requests.get(f"{BASE_URL}/{test_role}", headers=headers)
    logger.info(f"Get role by ID response: {response.status_code} {response.text}")
    assert response.status_code == 200, f"Expected 200, got {response.status_code}"
    assert response.json()["data"]["id"] == test_role

def test_update_role(auth_token, test_role):
    headers = {"Authorization": f"Bearer {auth_token}"}
    payload = {
        "name": "UPDATED_TEST_ROLE",
        "description": "Updated test role"
    }
    response = requests.put(f"{BASE_URL}/{test_role}", json=payload, headers=headers)
    logger.info(f"Update role response: {response.status_code} {response.text}")
    assert response.status_code == 200, f"Expected 200, got {response.status_code}"
    assert response.json()["data"]["name"] == "UPDATED_TEST_ROLE"

def test_add_permissions_to_role(auth_token, test_role):
    headers = {"Authorization": f"Bearer {auth_token}"}
    payload = ["PERMISSION_TEST"]
    response = requests.post(f"{BASE_URL}/{test_role}/permissions", json=payload, headers=headers)
    logger.info(f"Add permissions response: {response.status_code} {response.text}")
    assert response.status_code == 200, f"Expected 200, got {response.status_code}"
    # Assuming Role object has a permissions field; adjust if structure differs
    assert any(p["name"] == "PERMISSION_TEST" for p in response.json()["data"].get("permissions", []))

def test_remove_permissions_from_role(auth_token, test_role):
    headers = {"Authorization": f"Bearer {auth_token}"}
    payload = ["PERMISSION_TEST"]
    response = requests.delete(f"{BASE_URL}/{test_role}/permissions", json=payload, headers=headers)
    logger.info(f"Remove permissions response: {response.status_code} {response.text}")
    assert response.status_code == 200, f"Expected 200, got {response.status_code}"
    assert all(p["name"] != "PERMISSION_TEST" for p in response.json()["data"].get("permissions", []))

def test_create_permission(auth_token):
    headers = {"Authorization": f"Bearer {auth_token}"}
    payload = {
        "name": "TEST_PERMISSION",
        "description": "Test permission",
        "category": "TEST"
    }
    response = requests.post(f"{BASE_URL}/permissions", json=payload, headers=headers)
    logger.info(f"Create permission response: {response.status_code} {response.text}")
    assert response.status_code == 201, f"Expected 201, got {response.status_code}"
    assert response.json()["data"]["name"] == "TEST_PERMISSION"

def test_get_all_permissions(auth_token):
    headers = {"Authorization": f"Bearer {auth_token}"}
    response = requests.get(f"{BASE_URL}/permissions", headers=headers)
    logger.info(f"Get all permissions response: {response.status_code} {response.text}")
    assert response.status_code == 200, f"Expected 200, got {response.status_code}"
    assert isinstance(response.json()["data"], list)

def test_delete_role(auth_token, test_role):
    headers = {"Authorization": f"Bearer {auth_token}"}
    response = requests.delete(f"{BASE_URL}/{test_role}", headers=headers)
    logger.info(f"Delete role response: {response.status_code} {response.text}")
    assert response.status_code == 204, f"Expected 204, got {response.status_code}"