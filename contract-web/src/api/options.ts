import request from './index'

export function getOptions() {
  return request.get('/options')
}
