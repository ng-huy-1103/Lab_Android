import axios from 'axios'
import Utils from "../utils/Utils";

const API_URL = 'http://localhost:8000/api/v1'
const AUTH_URL = 'http://localhost:8000/auth'

class BackendService {

    login(login, password) {
        return axios.post(`${AUTH_URL}/login`, {login, password})
    }

    logout() {
        return axios.get(`${AUTH_URL}/logout`, { headers : {Authorization : Utils.getToken()}})
    }
}

export default new BackendService()